package com.aigilas.status.impl;import com.xna.wrapper.*;import java.util.*;import com.aigilas.creatures.*;import com.aigilas.strategies.*;import com.aigilas.entities.*;import com.aigilas.skills.*;import com.spx.core.*;    public class PoisonStatus extends IStatus    {        public PoisonStatus(ICreature target)        {        	super(target);        }@Override        public  void Update(){            super.Update();            _target.ApplyDamage(2.1f);        }    }