package com.aigilas.management;import com.xna.wrapper.*;import java.util.*;import com.spx.sprites.*;
    class SpriteInitializer implements ISpriteInitializer
    {
        private SpriteDefinition Make(int type, int index, int frames)
        {
            return new SpriteDefinition(type, index, frames);
        }
        public List<SpriteDefinition> GetSprites()
        {        	ArrayList<SpriteDefinition> result = new ArrayList<SpriteDefinition>(); 
            result.add(Make(SpriteType.EMPTY, 0, 1));
            result.add(Make(SpriteType.PLAYER_STAND, 1, 2));
            result.add(Make(SpriteType.FLOOR, 2, 1));
            result.add(Make(SpriteType.WALL, 3, 1));
            result.add(Make(SpriteType.UPSTAIRS, 4, 1));
            result.add(Make(SpriteType.DOWNSTAIRS, 5, 1));            
            result.add(Make(SpriteType.CREATURE, 6, 1));
            result.add(Make(SpriteType.ITEM, 7, 1));
            result.add(Make(SpriteType.SKILL_EFFECT,8, 1));
            result.add(Make(SpriteType.ALTAR, 9, 1));
            result.add(Make(SpriteType.ZORB, 10, 1));
            result.add(Make(SpriteType.MINION, 11, 1));
            result.add(Make(SpriteType.COMBO_MARKER, 13, 1));
            result.add(Make(SpriteType.WRATH, 14, 1));
            result.add(Make(SpriteType.HAND, 15, 1));                
            result.add(Make(SpriteType.ENVY, 16, 1));
            result.add(Make(SpriteType.SLOTH, 17, 1));
            result.add(Make(SpriteType.GREED, 18, 1));
            result.add(Make(SpriteType.GLUTTONY, 19, 1));
            result.add(Make(SpriteType.LUST, 20, 1));
            result.add(Make(SpriteType.PRIDE, 21, 1));
            result.add(Make(SpriteType.WHEEL, 22, 1));
            return result;
        }
    }
