package com.aigilas.creatures;import com.xna.wrapper.*;import java.util.*;import com.aigilas.skills.*;import com.aigilas.strategies.*;import com.aigilas.classes.*;import com.aigilas.management.*;import com.aigilas.entities.*;import com.aigilas.gods.*;import com.aigilas.creatures.*;import com.spx.core.*;import com.spx.devtools.*;
public     class Sloth extends  AbstractCreature
    {
        public Sloth()
            { super(AigilasActorType.SLOTH, SpriteType.SLOTH);            Compose(Elements.EARTH);
            Strengths(StatType.STRENGTH, StatType.STRENGTH);
            Add(SkillId.SERPENT_SUPPER);
        }
    }
