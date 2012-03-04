﻿using System.Collections.Generic;
namespace OGUR.Creatures
{
    public class Generate
    {
        public static List<int> Randoms = new List<int>() { OgurActorType.PEON, OgurActorType.ZORB };
    }
    public class OgurActorType
    {
        public const int MINION = 2;
        public const int PLAYER = 3;
        public const int ACID_NOZZLE = 4;
        public const int PEON = 5;
        public const int ZORB = 6;
        public const int NONPLAYER = 7;
        public const int DART_TRAP = 8;
    }
}