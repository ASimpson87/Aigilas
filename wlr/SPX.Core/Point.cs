﻿using System;
using System.Collections.Generic;
using Microsoft.Xna.Framework;
using System.Runtime.Serialization;



namespace SPX.Core
{
    [Serializable()]
    public class Point2
    {
        private static readonly Point2 Zero = new Point2(0, 0);
        private static readonly float halfHeight = GameManager.SpriteHeight/2;
        private static readonly float halfWidth= GameManager.SpriteWidth/ 2;

        public float X { get; private set; }
        public float Y { get; private set; }
        public float Weight { get; private set; }
        public int GridX { get; private set;}
        public int GridY { get; private set; }
        public float PosX { get; private set; }
        public float PosY { get; private set; }
        public float PosCenterX { get; private set; }
        public float PosCenterY { get; private set; }

        public static Point2[] _rotateTargets = 
            {
                new Point2(1, 0),
                new Point2(1, 1),
                new Point2(0, 1),
                new Point2(0,-1),
                new Point2(-1,-1),
                new Point2(-1, 0),
                new Point2(-1, 1),
                new Point2(1, -1)
            };

        public static readonly Point2[,] _locations = new Point2[GameManager.TileMapHeight, GameManager.TileMapWidth];

        public Point2(float x, float y, int weight = 0)
        {
            SetX(x);
            SetY(y);
            Weight = weight;
        }

        public Point2(Point2 target) : this(target.X, target.Y, 0) { }

        public Point2(Vector2 target) : this(target.X, target.Y, 0) { }
     
        public void Reset(float x, float y)
        {
            SetX(x);
            SetY(y);
        }

        public void Copy(Point2 point)
        {
            if (point != null)
            {
                SetX(point.X);
                SetY(point.Y);
            }
        }

        public Point2 Add(Point2 target)
        {
            return new Point2(GridX + target.GridX,GridY + target.GridY);
        }

        public Point2 Minus(Point2 target)
        {
            return new Point2(GridX - target.GridX, GridY - target.GridY);
        }

        public override int GetHashCode()
        {
            return GridX + 1000*GridY;
        }

        public override bool Equals(object obj)
        {
            if (obj.GetType()==typeof(Point2))
            {
                var target = (Point2) obj;
                return target.GridX == GridX && target.GridY == GridY;
            }
            return false;
        }

        public void SetX(float xValue)
        {
            X = xValue;
            var isGrid = (Math.Abs(X) < GameManager.TileMapWidth);
            PosX = (isGrid) ? X * GameManager.SpriteWidth : X;
            PosCenterX = PosX + halfWidth;
            GridX = (isGrid) ? (int)X : (int)(X / GameManager.SpriteWidth);
        }

        public void SetY(float yValue)
        {
            Y = yValue;
            var isGrid = (Math.Abs(Y) < GameManager.TileMapWidth);
            PosY = (isGrid) ? Y * GameManager.SpriteHeight: Y;
            PosCenterY = PosY+halfHeight;
            GridY = (isGrid) ? (int)Y : (int)(Y / GameManager.SpriteHeight);
        }

        public void SetWeight(float weight)
        {
            Weight = weight;
        }

        public bool IsZero()
        {
            return X == 0 && Y == 0;
        }

        public static float CalculateDistanceSquared(Point2 source, Point2 target)
        {
            return (float) (Math.Pow(source.PosY - target.PosY, 2) + Math.Pow(source.PosX - target.PosX, 2));
        }

        private static readonly List<Point2> _neighbors = new List<Point2>();
        public List<Point2> GetNeighbors()
        {
            if (_locations[0,0]==null)
            {
                for (int ii = 0; ii < GameManager.TileMapHeight; ii++)
                {
                    for (int jj = 0; jj < GameManager.TileMapWidth; jj++)
                    {
                        _locations[ii,jj] = new Point2(jj, ii);
                    }
                }
            }
            _neighbors.Clear();
            for (var ii = -1; ii < 2; ii++)
            {
                for (var jj = -1; jj < 2; jj++)
                {
                    if (ii != 0 || jj != 0)
                    {
                        _neighbors.Add(_locations[GridY + ii,GridX + jj]);
                    }
                }
            }
            return _neighbors;
        }

        public bool IsSameSpot(Point2 target)
        {
            return target.GridX == GridX && target.GridY == GridY;
        }

        public Point2 RotateClockwise()
        {
            if (GridX == 1)
            {
                if (GridY == -1)
                {
                    return _rotateTargets[0];
                }
                if (GridY == 0)
                {
                    return _rotateTargets[1];
                }
                if (GridY == 1)
                {
                    return _rotateTargets[2];
                }
            }
            if (GridX == -1)
            {
                if (GridY == -1)
                {
                    return _rotateTargets[3];
                }
                if (GridY == 0)
                {
                    return _rotateTargets[4];
                }
                if (GridY == 1)
                {
                    return _rotateTargets[5];
                }
            }
            if (GridX == 0)
            {
                if (GridY == 1)
                {
                    return _rotateTargets[6];
                }
                if (GridY == -1)
                {
                    return _rotateTargets[7];
                }
            }
            return Zero;
            /*
             * This is getting close, but the flipped Y coord is ticking me off.
            var theta = Math.PI / 4f;
            var currentRotation = Math.Atan2(-Y, X);
            Console.WriteLine(currentRotation);
            currentRotation -= theta;
            var x = (float)Math.Cos(currentRotation);
            var y = (float)Math.Sin(currentRotation);
            if(x!=0)
            {
                x = (1 / (Math.Abs(x))) * x;
            }
            if (y != 0)
            {
                y = (1 / (Math.Abs(y))) * y;
            }
            var result = new Point2(x,y);
            Console.WriteLine(result);
            return result;
             * */
        }

        public Point2 Rotate180()
        {
            return RotateClockwise().RotateClockwise().RotateClockwise().RotateClockwise();
        }

        public static float DistanceSquared(Point2 source, Point2 target)
        {
            return (float)(Math.Pow(source.PosX - target.PosX, 2) + Math.Pow(source.PosY - target.PosY, 2));
        }

        public override String ToString()
        {
            return "(gX,gY) - (posX,posY): (" + GridX + "," + GridY + ") - (" + PosX + "," + PosY + ")";
        }        
    }
}