package sps.text;

import com.badlogic.gdx.graphics.Color;
import sps.bridge.DrawDepth;
import sps.core.Point2;
import sps.core.Settings;
import sps.core.Spx;

class DefaultHudText extends Text {
    private final Color _color;
    private Point2 _origin;

    public DefaultHudText(float alpha) {
        _color = new Color(255f, 255f, 255f, alpha);
    }

    public void reset(String contents, int x, int y, Point2 origin) {
        _origin = (origin == null) ? Point2.Zero : origin;
        reset(contents, x, y);
    }

    @Override
    public int update() {
        return 1;
    }

    @Override
    public void draw() {
        float x = (int) _origin.X + _position.X + Settings.get().spriteWidth;
        float y = (int) _origin.Y + _position.Y + Settings.get().spriteHeight / 2;
        Spx.Renderer.drawString(_contents, new Point2(x, y), _color, 1f, DrawDepth.DefaultHudText);
    }
}
