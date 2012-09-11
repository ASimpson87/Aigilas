package aigilas.hud;

import java.util.HashMap;

import spx.bridge.DrawDepth;
import spx.core.SpxManager;
import spx.io.Input;
import spx.util.StringSquisher;
import spx.util.StringStorage;
import aigilas.creatures.ICreature;
import aigilas.items.Equipment;
import aigilas.items.GenericItem;
import aigilas.items.Inventory;
import aigilas.items.ItemClass;
import aigilas.management.Commands;

import com.badlogic.gdx.graphics.Color;

public class InventoryHud extends IHud {
	private int _currentClass = 0;
	private final Inventory _inventory;
	private final Equipment _equipment;
	private int _endingItem = 4;
	private int _startingItem = 0;
	private HashMap<GenericItem, Integer> _currentClassItems;
	private GenericItem _currentSelectedItem = null;

	private final EquipmentHud _equipHud;
	private final DeltasHud _deltas;

	public InventoryHud(ICreature owner, Inventory inventory, Equipment equipment) {
		super(owner, SpxManager.WindowWidth / 2, SpxManager.WindowHeight / 2);
		_inventory = inventory;
		_equipment = equipment;
		_deltas = new DeltasHud(owner, equipment);
		_equipHud = new EquipmentHud(owner, equipment);
		_currentClassItems = _inventory.GetItems(ItemClass.values()[_currentClass]);
	}

	public void Draw() {
		if (_isVisible) {
			SpxManager.Renderer.Draw(_menuBase, GetHudOrigin(), DrawDepth.HudBG, Color.BLACK, (int) SpxManager.GetCenter().X, (int) SpxManager.GetCenter().Y);
			_textHandler.Draw();
			_deltas.Draw();
			_equipHud.Draw();
		}
	}

	private void HandleInput() {
		if (Input.IsActive(Commands.CycleLeft, _parent.GetPlayerIndex())) {
			_currentClass--;
			if (_currentClass <= 0) {
				_currentClass = ItemClass.values().length - 2;
			}
			_startingItem = 0;
			_endingItem = 4;
			forceRefresh = true;
		}

		if (Input.IsActive(Commands.CycleRight, _parent.GetPlayerIndex())) {
			_currentClass++;
			if (_currentClass >= ItemClass.values().length) {
				_currentClass = 1;
			}
			_startingItem = 0;
			_endingItem = 4;
			forceRefresh = true;
		}

		if (Input.IsActive(Commands.MoveDown, _parent.GetPlayerIndex())) {
			if (_startingItem < _currentClassItems.size() - 1) {
				_startingItem++;
				_endingItem++;
				forceRefresh = true;
			}
		}

		if (Input.IsActive(Commands.MoveUp, _parent.GetPlayerIndex())) {
			if (_startingItem > 0) {
				_startingItem--;
				_endingItem--;
				forceRefresh = true;
			}
		}
		if (Input.IsActive(Commands.Confirm, _parent.GetPlayerIndex())) {
			_parent.Equip(_currentSelectedItem);
			forceRefresh = true;
		}
		if (Input.IsActive(Commands.Cancel, _parent.GetPlayerIndex())) {
			_parent.Drop(_currentSelectedItem);
			forceRefresh = true;
		}
	}

	public void Update() {
		if (_isVisible) {
			HandleInput();
			_textHandler.Update();
			_deltas.Update(_currentSelectedItem, forceRefresh);
			_equipHud.Update(forceRefresh);
			_textHandler.Clear();
			UpdateInventoryDisplay();
			if (forceRefresh) {
				forceRefresh = false;
			}
		}
	}

	@Override
	public void Toggle() {
		super.Toggle();
		_deltas.Toggle();
		_equipHud.Toggle();
		forceRefresh = true;
	}

	private static HashMap<Integer, String> __classStrings = new HashMap<>();

	private String GetClassDisplay() {
		if (!__classStrings.containsKey(_currentClass)) {
			__classStrings.put(_currentClass, ItemClass.values()[_currentClass].hudName());
		}
		return __classStrings.get(_currentClass);
	}

	private static final String __delim = ")";
	private static final String __equipDelim = "~";
	private static final String __seper = " x";
	private static final String __newline = "\n";

	private String displayString = "";
	private boolean forceRefresh = false;
	private String[] list = new String[10];

	private void UpdateInventoryDisplay() {
		_textHandler.WriteDefault(GetClassDisplay(), 20, 30, GetHudOrigin());
		_currentClassItems = _inventory.GetItems(ItemClass.values()[_currentClass]);
		if (_currentClassItems.size() > 0) {
			int ii = 0;
			if (forceRefresh) {
				StringSquisher.Clear();
				displayString = StringSquisher.Flush();
				int count = 0;
				for (GenericItem item : _currentClassItems.keySet()) {
					if (ii == _startingItem) {
						_currentSelectedItem = item;
					}
					if (!_equipment.IsRegistered(item) && _inventory.GetItemCount(item) <= 0) {
						continue;
					}

					if (ii >= _startingItem && ii < _endingItem && ii < _currentClassItems.keySet().size()) {
						count++;
						StringSquisher.Clear();
						StringSquisher.Squish(StringStorage.Get(ii), __delim, (_equipment.IsRegistered(item)) ? __equipDelim : "", item.Name);
						if (_currentClassItems.get(item) > -1) {
							StringSquisher.Squish(__seper, StringStorage.Get(_currentClassItems.get(item)));
						}
						StringSquisher.Squish(__newline);
						if (count < 10) {
							list[count] = StringSquisher.Flush();
						}
					}
					ii++;
				}

			}
			_textHandler.WriteDefault(displayString, 50, 60, GetHudOrigin());
			for (int jj = 0; jj < 10; jj++) {
				if (list[jj] != null) {
					_textHandler.WriteDefault(list[jj], 20, 60 + 40 * jj, GetHudOrigin());
				}
			}
		}
	}
}
