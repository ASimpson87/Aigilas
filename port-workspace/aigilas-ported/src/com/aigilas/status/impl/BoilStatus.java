package com.aigilas.status.impl;import com.xna.wrapper.*;import java.util.*;import com.aigilas.creatures.*;import com.aigilas.strategies.*;import com.aigilas.entities.*;import com.aigilas.skills.*;import com.spx.core.*;    public class BoilStatus  extends  IStatus    {        private int previousStrategy;        private float _previousHealth = 0;        private boolean _countDownFailed = false;        private static final int _countdownMax = 10;        private int _countdown = _countdownMax;                public BoilStatus(ICreature target)            { super(target);                    }@Override        public  void Setup()        {            super.Setup();            previousStrategy = _target.GetStrategyId();            _target.SetStrategy(StrategyFactory.Create(Strategy.Null, _target));            _target.GetTargets().AddTargetTypes(AigilasActorType.PLAYER);        }@Override        public  void Cleanup()        {            super.Cleanup();            if (!_countDownFailed)            {                _target.GetTargets().FindClosest().ApplyDamage(30);            }            _target.SetStrategy(StrategyFactory.Create(previousStrategy, _target));        }@Override        public  void Update()        {            super.Update();            if (_target.IsCooledDown())            {                _countdown--;                _target.Write(com.spx.Util.StringStorage.Get(_strength));                if (_countdown <= 0)                {                    _countdown = _countdownMax;                }            }            if (_target.Get(StatType.HEALTH) < _previousHealth)            {                _countDownFailed = true;                Cleanup();            }            _previousHealth = _target.Get(StatType.HEALTH);        }    }