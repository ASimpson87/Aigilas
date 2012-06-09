﻿using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Microsoft.Xna.Framework;

namespace Agilas.Entities
{
    public class Elements
    {
        public const int NORMAL = 0;
        public const int FIRE = 1;
        public const int WATER = 2;
        public const int EARTH = 3;
        public const int AIR = 4;
        public const int LIGHT = 5;
        public const int DARK = 6;
        public const int PHYSICAL = 7;
        public const int MENTAL = 8;

        public static readonly int[] Values = 
        {
            NORMAL,
            FIRE,
            WATER,
            EARTH,
            AIR,
            LIGHT,
            DARK,
            PHYSICAL,
            MENTAL
        };

        public static readonly Color[] Colors =
        {
            Color.Gray,
            Color.Red,
            Color.Blue,
            Color.Green,
            Color.Yellow,
            Color.White,
            Color.Black,
            Color.Orange,
            Color.Purple
        };

    }
}