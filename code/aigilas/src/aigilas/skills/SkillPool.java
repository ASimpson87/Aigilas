package aigilas.skills;import java.util.ArrayList;import java.util.HashMap;import java.util.List;import aigilas.creatures.ICreature;public class SkillPool {	private List<String> _skills = new ArrayList<String>();	private int _currentSkillSlot = 0;	private ICreature _owner;	private HashMap<String, Integer> _usageCounter = new HashMap<String, Integer>();	private HashMap<Integer, String> _hotSkills = new HashMap<Integer, String>();	public SkillPool(ICreature owner) {		_owner = owner;	}	public void Add(String skill) {		if (skill.equals(null) && _skills.size() == 0) {			_skills.add(SkillId.NO_SKILL);			FindCurrent();			return;		}		if (!_skills.contains(skill)) {			_skills.add(skill);		}		if (_skills.contains(SkillId.NO_SKILL)) {			_skills.remove(SkillId.NO_SKILL);			_currentSkillSlot = _skills.indexOf(skill);		}	}	private String FindCurrent() {		return _skills.get(_currentSkillSlot);	}	public void Add(List<String> getLevelSkills) {		if (getLevelSkills.size() == 0) {			_skills.add(SkillId.NO_SKILL);			return;		}		for (String skill : getLevelSkills) {			if (!_skills.contains(skill)) {				_skills.add(skill);			}		}	}	public void Cycle(int velocity) {		_currentSkillSlot = (_currentSkillSlot + velocity) % _skills.size();		if (_currentSkillSlot < 0) {			_currentSkillSlot = _skills.size() - 1;		}		FindCurrent();	}	public String GetActiveName() {		return _skills.size() > 0 ? FindCurrent() : "No Skill";	}	private void RemoveNone() {		_skills.remove(SkillId.NO_SKILL);	}	public void UseActive() {		if (FindCurrent() == SkillId.NO_SKILL) {			RemoveNone();			_currentSkillSlot = 0;		}		if (_skills.size() > 0) {			UseSkill(FindCurrent());		}	}	private void UseSkill(String skillId) {		SkillFactory.Create(FindCurrent()).Activate(_owner);		if (!_usageCounter.containsKey(skillId)) {			_usageCounter.put(skillId, 0);		}		_usageCounter.put(skillId, _usageCounter.get(skillId) + 1);	}	String _leastUsed;	public void RemoveLeastUsed() {		for (String skillId : _skills) {			if (!_usageCounter.containsKey(skillId)) {				_usageCounter.put(skillId, 0);			}		}		_leastUsed = null;		for (String key : _usageCounter.keySet()) {			if ((_leastUsed == null || _leastUsed == SkillId.FORGET_SKILL || _usageCounter					.get(key) < _usageCounter.get(_leastUsed))					&& key != SkillId.FORGET_SKILL) {				_leastUsed = key;			}		}		if (_leastUsed != SkillId.FORGET_SKILL) {			_skills.remove(_leastUsed);		}	}	public int Count() {		return _skills.size();	}	public void MakeActiveSkillHot(int hotSkillSlot) {		if (!_hotSkills.containsKey(hotSkillSlot)) {			_hotSkills.put(hotSkillSlot, GetActiveName());		}		_hotSkills.put(hotSkillSlot, GetActiveName());	}	public boolean SetHotSkillsActive(int hotkey) {		if (_hotSkills.containsKey(hotkey)) {			for (int ii = 0; ii < _skills.size(); ii++) {				if (_skills.get(ii) == _hotSkills.get(hotkey)) {					_currentSkillSlot = ii;					FindCurrent();					return true;				}			}		}		return false;	}	public String GetHotSkillName(int hotSkillSlot) {		if (_hotSkills.containsKey(hotSkillSlot)) {			return _hotSkills.get(hotSkillSlot);		}		return "";	}	public float GetCurrentCost() {		return SkillFactory.GetCost(FindCurrent());	}}