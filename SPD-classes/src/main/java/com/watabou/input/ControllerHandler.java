/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2022 Evan Debenham
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package com.watabou.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerListener;
import com.badlogic.gdx.controllers.ControllerMapping;
import com.badlogic.gdx.controllers.Controllers;
import com.watabou.noosa.Game;
import com.watabou.noosa.ui.Cursor;
import com.watabou.utils.DeviceCompat;
import com.watabou.utils.PointF;

public class ControllerHandler implements ControllerListener {

	public enum ControllerType {
		XBOX,
		PLAYSTATION,
		NINTENDO,
		OTHER
	}

	public static ControllerType lastUsedType = ControllerType.OTHER;
	public static boolean controllerActive = false;

	private static void setControllerType(Controller controller){
		if (controller.getName().contains("Xbox")){
			lastUsedType = ControllerType.XBOX;
		} else if (controller.getName().contains("PS")){
			lastUsedType = ControllerType.PLAYSTATION;
		} else if (controller.getName().contains("Nintendo")){
			lastUsedType = ControllerType.NINTENDO;
		} else {
			lastUsedType = ControllerType.OTHER;
		}
	}

	public static boolean controllersSupported() {
		if (DeviceCompat.isAndroid() && Gdx.app.getVersion() < 16) {
			return false;
		} else {
			return true;
		}
	}

	public static boolean isControllerConnected(){
		return controllersSupported() && Controllers.getCurrent() != null;
	}

	@Override
	public void connected(Controller controller) {
		controllerActive = true;
		setControllerType(controller);
	}

	@Override
	public void disconnected(Controller controller) {

	}

	@Override
	public boolean buttonDown(Controller controller, int buttonCode) {
		setControllerType(controller);
		controllerActive = true;
		int keyCode = buttonToKey(controller, buttonCode);
		if (keyCode != Input.Keys.UNKNOWN){
			KeyEvent.addKeyEvent(new KeyEvent(keyCode, true));
			return true;
		}
		return false;
	}

	@Override
	public boolean buttonUp(Controller controller, int buttonCode) {
		setControllerType(controller);
		controllerActive = true;
		int keyCode = buttonToKey(controller, buttonCode);
		if (keyCode != Input.Keys.UNKNOWN){
			KeyEvent.addKeyEvent(new KeyEvent(keyCode, false));
			return true;
		}
		return false;
	}

	public static PointF leftStickPosition = new PointF();
	public static PointF rightStickPosition = new PointF();

	private float L2Trigger = 0f;
	private float R2Trigger = 0f;

	//FIXME these axis mappings seem to be wrong on Android (and iOS?) in some cases
	@Override
	public boolean axisMoved(Controller controller, int axisCode, float value) {
		setControllerType(controller);
		ControllerMapping mapping = controller.getMapping();
		if (mapping.axisRightX == axisCode)         rightStickPosition.x = value;
		else if (mapping.axisRightY == axisCode)    rightStickPosition.y = value;
		else if (mapping.axisLeftX == axisCode)     leftStickPosition.x = value;
		else if (mapping.axisLeftY == axisCode)     leftStickPosition.y = value;

		//L2 and R2 triggers on Desktop
		else if (axisCode == 4) {

			if (L2Trigger < 0.5f && value >= 0.5f){
				KeyEvent.addKeyEvent(new KeyEvent(Input.Keys.BUTTON_L2, true));
				controllerActive = true;
			} else if (L2Trigger >= 0.5f && value < 0.5f){
				KeyEvent.addKeyEvent(new KeyEvent(Input.Keys.BUTTON_L2, false));
				controllerActive = true;
			}
			L2Trigger = value;

		} else if (axisCode == 5){

			if (R2Trigger < 0.5f && value >= 0.5f){
				KeyEvent.addKeyEvent(new KeyEvent(Input.Keys.BUTTON_R2, true));
				controllerActive = true;
			} else if (R2Trigger >= 0.5f && value < 0.5f){
				KeyEvent.addKeyEvent(new KeyEvent(Input.Keys.BUTTON_R2, false));
				controllerActive = true;
			}
			R2Trigger = value;

		}
		return true;
	}

	//we use a separate variable as Gdx.input.isCursorCatched only works on desktop
	private static boolean controllerPointerActive = false;
	private static PointF controllerPointerPos;

	public static void setControllerPointer( boolean active ){
		if (active) controllerActive = true;
		if (controllerPointerActive == active) return;
		controllerPointerActive = active;
		if (active){
			Gdx.input.setCursorCatched(true);
			controllerPointerPos = new PointF(Game.width/2, Game.height/2);
		} else if (!Cursor.isCursorCaptured()) {
			Gdx.input.setCursorCatched(false);
		}
	}

	public static boolean controllerPointerActive(){
		return controllerPointerActive && !Cursor.isCursorCaptured();
	}

	public static PointF getControllerPointerPos(){
		return controllerPointerPos.clone();
	}

	public static void updateControllerPointer(PointF pos, boolean sendEvent){
		controllerPointerPos.set(pos);
		if (sendEvent) {
			controllerActive = true;
			PointerEvent.addPointerEvent(new PointerEvent((int) controllerPointerPos.x, (int) controllerPointerPos.y, 10_000, PointerEvent.Type.HOVER, PointerEvent.NONE));
		}
	}

	//converts controller button codes to keyEvent codes
	public static int buttonToKey(Controller controller, int btnCode){
		ControllerMapping mapping = controller.getMapping();
		if (btnCode == mapping.buttonA)         return Input.Keys.BUTTON_A;
		if (btnCode == mapping.buttonB)         return Input.Keys.BUTTON_B;
		//C button?
		if (btnCode == mapping.buttonX)         return Input.Keys.BUTTON_X;
		if (btnCode == mapping.buttonY)         return Input.Keys.BUTTON_Y;
		if (btnCode == mapping.buttonBack)      return Input.Keys.BUTTON_SELECT;
		if (btnCode == mapping.buttonStart)     return Input.Keys.BUTTON_START;

		if (btnCode == mapping.buttonL1)        return Input.Keys.BUTTON_L1;
		if (btnCode == mapping.buttonL2)        return Input.Keys.BUTTON_L2;
		if (btnCode == mapping.buttonR1)        return Input.Keys.BUTTON_R1;
		if (btnCode == mapping.buttonR2)        return Input.Keys.BUTTON_R2;

		//we add 1000 here to make these keys distinct from Keys.UP, Keys.DOWN, etc..
		if (btnCode == mapping.buttonDpadUp)    return Input.Keys.DPAD_UP       + 1000;
		if (btnCode == mapping.buttonDpadDown)  return Input.Keys.DPAD_DOWN     + 1000;
		if (btnCode == mapping.buttonDpadLeft)  return Input.Keys.DPAD_LEFT     + 1000;
		if (btnCode == mapping.buttonDpadRight) return Input.Keys.DPAD_RIGHT    + 1000;

		if (btnCode == mapping.buttonLeftStick) return Input.Keys.BUTTON_THUMBL;
		if (btnCode == mapping.buttonRightStick)return Input.Keys.BUTTON_THUMBR;

		return Input.Keys.UNKNOWN;
	}

	public static boolean icControllerKey(int keyCode){
		if (keyCode >= Input.Keys.BUTTON_A && keyCode <= Input.Keys.BUTTON_MODE){
			return true;
		}

		if (keyCode >= Input.Keys.DPAD_UP+1000 && keyCode <= Input.Keys.DPAD_RIGHT+1000){
			return true;
		}

		return false;
	}

	public static String customButtonName(int keyCode){
		if (lastUsedType == ControllerType.PLAYSTATION){
			if (keyCode == Input.Keys.BUTTON_A){
				return "Cross Button";
			} else if (keyCode == Input.Keys.BUTTON_B){
				return "Circle Button";
			} else if (keyCode == Input.Keys.BUTTON_X){
				return "Square Button";
			} else if (keyCode == Input.Keys.BUTTON_Y){
				return "Triangle Button";
			}
		} else if (lastUsedType == ControllerType.XBOX){
			if (keyCode == Input.Keys.BUTTON_L1){
				return "Left Bumper";
			} else if (keyCode == Input.Keys.BUTTON_L2){
				return "Left Trigger";
			} else if (keyCode == Input.Keys.BUTTON_R1){
				return "Right Bumper";
			} else if (keyCode == Input.Keys.BUTTON_R2){
				return "Right Trigger";
			}
		}

		if (keyCode == Input.Keys.DPAD_UP + 1000){
			return Input.Keys.toString(Input.Keys.DPAD_UP);
		} else if (keyCode == Input.Keys.DPAD_DOWN + 1000){
			return Input.Keys.toString(Input.Keys.DPAD_DOWN);
		} else if (keyCode == Input.Keys.DPAD_LEFT + 1000){
			return Input.Keys.toString(Input.Keys.DPAD_LEFT);
		} else if (keyCode == Input.Keys.DPAD_RIGHT + 1000){
			return Input.Keys.toString(Input.Keys.DPAD_RIGHT);
		}

		return null;
	}
}
