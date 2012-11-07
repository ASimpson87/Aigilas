package aigilas.hud;

import aigilas.creatures.BaseCreature;
import aigilas.creatures.StatType;
import sps.bridge.Commands;
import com.badlogic.gdx.graphics.Color;
import sps.bridge.DrawDepth;
import sps.core.Point2;
import sps.core.Settings;
import sps.graphics.Renderer;
import sps.text.Text;
import sps.text.TextPool;

public class SkillHud extends BaseHud {
    private static final String __separator = "|";
    private Text skillHeader;
    private String lastSkillString;

    private Point2 _energyPosition = new Point2();

    public SkillHud(BaseCreature owner) {
        super(owner, Settings.get().spriteWidth, Renderer.VirtualHeight / 4);
        _energyPosition = new Point2(getMeterAnchor().X, getMeterAnchor().Y - Renderer.VirtualHeight / 4);
    }

    private int calculateHeight(StatType statType) {
        return (int) (((float) _parent.get(statType) / _parent.getMax(statType)) * _dimensions.Y);
    }

    private int costOfCurrentSkill() {
        return (int) (_parent.getCurrentSkillCost() / (float) _parent.getMax(StatType.Energy) * _dimensions.Y);
    }

    private String getSkillStrings() {
        return "S:" + _parent.getActiveSkillName() + __separator + "Z:" + _parent.getHotSkillName(Commands.HotSkill1) + __separator + "X:" + _parent.getHotSkillName(Commands.HotSkill2) + __separator + "C:" + _parent.getHotSkillName(Commands.HotSkill3) + __separator;
    }

    public void update() {
        if (_isVisible && (lastSkillString == null || !getSkillStrings().equals(lastSkillString))) {
            if (skillHeader != null) {
                skillHeader.hide();
            }
            skillHeader = TextPool.get().write(getSkillStrings(), getSkillAnchor().add(Settings.get().spriteWidth, 0));
        }
    }

    @Override
    public void draw() {
        if (!_isVisible) {
            if (skillHeader != null) {
                skillHeader.hide();
            }
            return;
        }

        Renderer.get().draw(_menuBase, getMeterAnchor(), DrawDepth.HudBG, Color.GREEN, Settings.get().spriteWidth, calculateHeight(StatType.Health));
        Renderer.get().draw(_menuBase, _energyPosition, DrawDepth.HudBG, Color.BLUE, Settings.get().spriteWidth, calculateHeight(StatType.Energy));
        Renderer.get().draw(_menuBase, _energyPosition, DrawDepth.HudBG, Color.YELLOW, Settings.get().spriteWidth / 2, costOfCurrentSkill());
    }
}
