package spx.sprites;import spx.core.GameManager;public class SpriteInfo {	public int X, Y, SpriteIndex, MaxFrame;	public SpriteInfo(int spriteIndex, int maxFrame) {		X = GameManager.SpriteWidth;		Y = GameManager.SpriteHeight;		SpriteIndex = spriteIndex;		MaxFrame = maxFrame;	}}