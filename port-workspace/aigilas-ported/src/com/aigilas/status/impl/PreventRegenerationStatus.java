package com.aigilas.status.impl;import com.xna.wrapper.*;import java.util.*;import com.aigilas.creatures.*;import com.aigilas.strategies.*;import com.aigilas.entities.*;import com.aigilas.skills.*;import com.spx.core.*;    public class PreventRegenerationStatus  extends  IStatus    {        public PreventRegenerationStatus(ICreature target)            { super(target);            _prevents.add(CreatureAction.Regeneration);        }    }