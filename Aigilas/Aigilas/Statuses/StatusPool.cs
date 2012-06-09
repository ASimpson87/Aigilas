﻿using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Agilas.Creatures;

namespace Agilas.Statuses
{
    public class StatusPool
    {
        private List<IStatus> _statuses = new List<IStatus>();

        public bool Allows(OAction action)
        {
            for (int ii = 0; ii < _statuses.Count(); ii++)
            {
                if (_statuses[ii].Prevents(action))
                {
                    return false;
                }
            }
            return true;
        }

        public void Add(IStatus status)
        {
            _statuses.Add(status);
        }

        public void Update()
        {
            for (int ii = 0; ii < _statuses.Count(); ii++)
            {
                _statuses[ii].Update();
                if (!_statuses[ii].IsActive())
                {
                    _statuses.Remove(_statuses[ii]);
                    ii--;
                }
            }
        }

        public void Act()
        {
            for (int ii = 0; ii < _statuses.Count(); ii++)
            {
                _statuses[ii].Act();
            }
        }

        public void PassOn(ICreature target,StatusComponent componentType)
        {
            for (int ii = 0; ii < _statuses.Count; ii++)
            {
                _statuses[ii].PassOn(target, componentType);
            }
        }

        public bool IsElementBlocked(int element)
        {
            return _statuses.Any(s => s.IsElementBlocked(element));
        }
    }
}