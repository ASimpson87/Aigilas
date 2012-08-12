package com.aigilas.management;import com.xna.wrapper.*;import java.util.*;import com.spx.core.*;import com.spx.io.*;
    public class InputInitializer implements IInputInitializer
    {
        private CommandDefinition Make(int command, Keys key, Buttons button, int lockContext)
        {
            return new CommandDefinition(command, key, button, lockContext);
        }

        public List<CommandDefinition> GetCommands()
        {        	ArrayList<CommandDefinition> result = new ArrayList<CommandDefinition>();
            result.add(Make(Commands.MoveUp,Keys.Up,Buttons.LeftThumbstickUp,Contexts.Nonfree));
            result.add(Make(Commands.MoveDown,Keys.Down,Buttons.LeftThumbstickDown,Contexts.Nonfree));
            result.add(Make(Commands.MoveLeft,Keys.Left,Buttons.LeftThumbstickLeft,Contexts.Nonfree));
            result.add(Make(Commands.MoveRight,Keys.Right,Buttons.LeftThumbstickRight,Contexts.Nonfree));
            result.add(Make(Commands.Confirm,Keys.Space,Buttons.RightTrigger,Contexts.All));
            result.add(Make(Commands.Inventory,Keys.E,Buttons.DPadUp,Contexts.All));
            result.add(Make(Commands.Cancel,Keys.R,Buttons.X,Contexts.All));
            result.add(Make(Commands.Start,Keys.Enter,Buttons.Start,Contexts.All));
            result.add(Make(Commands.Back,Keys.Back,Buttons.Back,Contexts.All));
            result.add(Make(Commands.CycleRight,Keys.D,Buttons.RightShoulder,Contexts.All));
            result.add(Make(Commands.CycleLeft,Keys.A,Buttons.LeftShoulder,Contexts.All));
            result.add(Make(Commands.Skill,Keys.S,Buttons.A,Contexts.All));
            result.add(Make(Commands.LockSkill,Keys.RightControl,Buttons.LeftTrigger,Contexts.All));
            result.add(Make(Commands.HotSkill1,Keys.D1,Buttons.X,Contexts.All));
            result.add(Make(Commands.HotSkill2,Keys.D2,Buttons.Y,Contexts.All));
            result.add(Make(Commands.HotSkill3,Keys.D3,Buttons.B,Contexts.All));
            result.add(Make(Commands.ToggleDevConsole,Keys.OemTilde,Buttons.DPadDown,Contexts.All));              return result;
        }
    }
