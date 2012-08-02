﻿using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Lidgren.Network;
using SPX.Core;
using SPX.DevTools;
using System.Threading;
using System.Runtime.Serialization.Formatters.Binary;
using System.IO;

namespace SPX.IO
{
    public class ClientMessageType
    {
        public const byte CONNECT = 1;
        public const byte MOVEMENT = 2;
        public const byte START_GAME = 3;
        public const byte CHECK_STATE = 4;
        public const byte PLAYER_COUNT = 5;
        public const byte SYNC_STATE = 6;
    }

    public static class CmtString
    {

        public static string Get(byte messageType)
        {
            switch(messageType)
            {
                case ClientMessageType.CONNECT: return "CONNECT";
                case ClientMessageType.MOVEMENT: return "MOVEMENT";
                case ClientMessageType.START_GAME: return "START_GAME";
                case ClientMessageType.CHECK_STATE: return "CHECK_STATE";
                case ClientMessageType.PLAYER_COUNT: return "PLAYER_COUNT";
            }
            return "INVALID MESSAGE TYPE";
        }
    }

    public class Client
    {
        public const bool DEBUG = false;
        private static Client __instance;
        public static Client Get()
        {
            if (__instance == null)
            {
                __instance = new Client();
                while (!__instance.IsConnected()) 
                {
                    __instance.Update();
                }
            }
            return __instance;
        }

        private NetClient _client;
        private NetPeerConfiguration _config;
        private NetIncomingMessage _message;
        private NetOutgoingMessage _outMessage;        
        private Int32 _initialPlayerIndex;
        private bool _isGameStarting;
        private bool _isConnected;

        private Dictionary<int, Dictionary<int, bool>> _playerStatus = new Dictionary<int, Dictionary<int, bool>>();

        private Client()
        {
            _config = new NetPeerConfiguration(Server.ConnectionName);
            for (int ii = 0; ii < MessageContents.PlayerMax; ii++)
            {
                _playerStatus.Add(ii, new Dictionary<int, bool>());
                for (int jj = 0; jj < MessageContents.CommandMax; jj++)
                {
                    _playerStatus[ii].Add(jj, false);
                }
            }
            _client = new NetClient(_config);
            _client.Start();
            var init = _client.CreateMessage();
            init.Write(ClientMessageType.CONNECT);      
            _client.Connect("localhost", Server.Port, init);
        }

        //Client<->Game communication
        public bool IsGameStarting()
        {
            return _isGameStarting;
        }

        public bool IsConnected()
        {
            return _isConnected;
        }

        public bool NextTurn()
        {
            bool result = false;
            _contents.Clear();
            Update();
            if (_contents.MessageType == ClientMessageType.SYNC_STATE)
            {
                DevConsole.Get().Add("CLIENT: Synced : " + _contents.TurnCount);
            }
            return _contents.MessageType == ClientMessageType.SYNC_STATE;
        }

        private void InitPlayer(int playerIndex, int command)
        {
            if (!_playerStatus.ContainsKey(playerIndex))
            {
                _playerStatus.Add(playerIndex, new Dictionary<int, bool>());
            }
            if (!_playerStatus[playerIndex].ContainsKey(command))
            {
                _playerStatus[playerIndex].Add(command, false);
            }
        }

        public int GetFirstPlayerIndex()
        {
            return _initialPlayerIndex;
        }
        

        //Client<->Server communication
        public bool IsActive(int command, int playerIndex)
        {
            if (_playerStatus.ContainsKey(playerIndex) && _playerStatus[playerIndex].ContainsKey(command))
            {
                return _playerStatus[playerIndex][command];
            }
            return false;
        }

        public void SetState(int command, int playerIndex, bool isActive)
        {
            InitPlayer(playerIndex, command);
            if (_playerStatus[playerIndex][command] != isActive)
            {
                if(DEBUG)Console.WriteLine("CLIENT: Moves: CMD({0}) PI({1}) AC({2})", command, playerIndex, isActive);
                _playerStatus[playerIndex][command] = isActive;
                SendMessage(MessageContents.CreateMovement(command, playerIndex, isActive));
            }
        }

        int _playerCount = 0;
        public int GetPlayerCount()
        {
            if (_playerCount == 0)
            {
                SendMessage(MessageContents.CreatePlayerCount(0));
                AwaitReply(ClientMessageType.PLAYER_COUNT);
                _playerCount = _contents.PlayerCount;
            }
            return _playerCount;
        }

        public void StartGame()
        {
            SendMessage(MessageContents.Create(ClientMessageType.START_GAME));
        }

        private void SendMessage(MessageContents contents)
        {
            _outMessage = _client.CreateMessage(MessageContents.ByteCount);
            contents.Serialize(_outMessage);
            _client.SendMessage(_outMessage, NetDeliveryMethod.ReliableOrdered);
        }

        //Be careful using this method.
        //If the server doesn't reply at some point with the messageType you expect
        //Then the client will hang in an infinite loop.

        private void AwaitReply(byte messageType)
        {
            if (DEBUG) Console.WriteLine("CLIENT: Waiting for " + CmtString.Get(messageType));          
            while (true)
            {
                if (DEBUG) Console.WriteLine("CLIENT: Waiting");
                _client.MessageReceivedEvent.WaitOne();
                _message = _client.ReadMessage();
                if (_message != null)
                {
                   
                    if (_message.MessageType == NetIncomingMessageType.Data)
                    {
                        _contents.Deserialize(_message);
                        if (_contents.MessageType == messageType)
                        {
                            if (DEBUG) Console.WriteLine("CLIENT: Right message received");
                            return;
                        }
                        else
                        {
                            if(DEBUG)Console.WriteLine("CLIENT: Wrong message received: "+_contents.MessageType+" Expected: "+messageType);
                            HandleResponse(_contents);
                        }                            
                    }
                    else
                    {
                        if (DEBUG) Console.WriteLine("CLIENT: Unexpected : " + _message.MessageType + ": " + _message.ReadString());
                    }
                    _client.Recycle(_message);
                }                
            }
        }

        private void HandleResponse(MessageContents contents)
        {
            switch (contents.MessageType)
            {
                case ClientMessageType.CONNECT:
                    if (DEBUG) Console.WriteLine("CLIENT: Handshake successful. Starting player id: " + contents.PlayerIndex);
                    RNG.Seed(contents.RngSeed);
                    _initialPlayerIndex = contents.PlayerCount;
                    _isConnected = true;
                    break;
                case ClientMessageType.START_GAME:
                    Console.WriteLine("CLIENT: Start game reply has been received");
                    _isGameStarting = true;
                    break;
                case ClientMessageType.SYNC_STATE:
                    if (DEBUG) Console.WriteLine("CLIENT: Input state received");
                    contents.ReadPlayerState(ref _playerStatus);
                    break;
                default:
                    break;
            }
        }

        private MessageContents _contents = MessageContents.Empty();
        public void Update()
        {
            while ((_message = _client.ReadMessage()) != null && _message.MessageType == NetIncomingMessageType.Data)
            {
                _contents.Deserialize(_message);
                HandleResponse(_contents);
                _client.Recycle(_message);
            }
        }        
    }
}
