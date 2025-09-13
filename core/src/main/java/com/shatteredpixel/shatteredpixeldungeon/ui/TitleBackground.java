/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2025 Evan Debenham
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

package com.shatteredpixel.shatteredpixeldungeon.ui;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.watabou.gltextures.TextureCache;
import com.watabou.noosa.Game;
import com.watabou.noosa.Group;
import com.watabou.noosa.Image;
import com.watabou.noosa.TextureFilm;
import com.watabou.noosa.ui.Component;
import com.watabou.utils.Random;
import com.watabou.utils.RectF;

import java.util.ArrayList;

//TODO still a couple of refinements here:
// now that all assets have 2x layers there's a lot of copypasta, surely logic can be shared in more places
// asset picking isn't ideal, more sophisticated algorithm should help with this
// back layers are dark, which works well on desktop but may be hard to see on mobile
public class TitleBackground extends Component {

	public static float SCROLL_SPEED	= 15f;

	private float density = 1f;

	//Arch back layer
	private static final TextureFilm ARCH_FILM = new TextureFilm(Assets.Splashes.Title.ARCHS, 333, 100);
	private static Group archLayer;
	private static ArrayList<Image> archs;

	//Cluster far layer
	private static TextureFilm CLUSTER_FILM = new TextureFilm(Assets.Splashes.Title.BACK_CLUSTERS, 450, 250);
	private static ArrayList<Image> clusters;
	private static Group clusterLayer;

	private static ArrayList<Image> clustersFar;
	private static Group clustersFarLayer;

	//Small far layer
	private static TextureFilm SMALL_FILM = new TextureFilm(Assets.Splashes.Title.FRONT_SMALL, 112, 116);
	private static ArrayList<Image> smallFars;
	private static Group smallFarLayer;

	//Mixed Item middle layer 1
	private static TextureFilm MID_FILM = new TextureFilm(Assets.Splashes.Title.MID_MIXED, 273, 242);
	private static ArrayList<Image> mids1;
	private static Group mids1Layer;
	//Mixed Item middle layer 2
	private static ArrayList<Image> mids2;
	private static Group mids2Layer;

	//Small Item front layer
	private static ArrayList<Image> smallCloses;
	private static Group smallCloseLayer;

	private static boolean wasLandscape;
	private static float oldBaseScale = 1;
	private static float oldWidth = 0;

	public static void reset(){
		archs = null;
		clusters = null;
		clustersFar = null;
		smallFars = null;
		mids1 = null;
		mids2 = null;
		smallCloses = null;
	}

	public TitleBackground(int width, int height){
		super();
		x = y = 0;
		this.width = width;
		this.height = height;
		setupObjects();
	}

	protected void setupObjects() {

		boolean landscape = width > height;

		float scale = height / 450f;

		//we reset in this case as scale changes
		if (archs != null && (landscape != wasLandscape)){
			archs = null;
			clustersFar = null;
			clusters = null;
			smallFarLayer = null;
			mids1 = null;
			mids2 = null;
			smallCloses = null;
		}
		wasLandscape = landscape;

		archLayer = new Group();
		if (archs == null) {
			archs = new ArrayList<>();
		} else {
			convertArchLayer(archs, archLayer, scale);
		}
		add(archLayer);

		Image darkness = new Image(TextureCache.createGradient(0x00000000, 0x11000000, 0x22000000, 0x33000000, 0x44000000, 0x88000000));
		darkness.angle = 90;
		darkness.x = width;
		darkness.scale.x = height/6f;
		darkness.scale.y = width;
		add(darkness);

		if (!landscape){
			scale /= 1.5f;
			oldBaseScale /= 1.5f;
		}

		density = width / (800f * scale);
		density = (density+0.5f)/1.5f; //pull density 33% of the way toward 1 if it is beyond it

		clustersFarLayer = new Group();
		if (clustersFar == null) {
			clustersFar = new ArrayList<>();
		} else {
			convertFloatingLayer(clustersFar, clustersFarLayer, scale, oldWidth);
		}
		add(clustersFarLayer);

		clusterLayer = new Group();
		if (clusters == null) {
			clusters = new ArrayList<>();
		} else {
			convertFloatingLayer(clusters, clusterLayer, scale, oldWidth);
		}
		add(clusterLayer);

		smallFarLayer = new Group();
		if (smallFars == null){
			smallFars = new ArrayList<>();
		} else {
			convertFloatingLayer(smallFars, smallFarLayer, scale, oldWidth);
		}
		add(smallFarLayer);

		mids1Layer = new Group();
		if (mids1 == null){
			mids1 = new ArrayList<>();
		} else {
			convertFloatingLayer(mids1, mids1Layer, scale, oldWidth);
		}
		add(mids1Layer);

		mids2Layer = new Group();
		if (mids2 == null){
			mids2 = new ArrayList<>();
		} else {
			convertFloatingLayer(mids2, mids2Layer, scale, oldWidth);
		}
		add(mids2Layer);

		smallCloseLayer = new Group();
		if (smallCloses == null){
			smallCloses = new ArrayList<>();
		} else {
			convertFloatingLayer(smallCloses, smallCloseLayer, scale, oldWidth);
		}
		add(smallCloseLayer);

		oldWidth = width/scale;
		if (!landscape){
			scale *= 1.5f;
		}
		oldBaseScale = scale;

	}

	//*** Logic for converting images between scene resets. ***

	//creates a new instance of a disposed of image, for recreation of this component after scene transition
	protected Image convertImage(Image oldImg, float newBaseScale){
		Image newImg = new Image(oldImg.texture);
		newImg.frame(oldImg.frame());
		float oldScale = oldImg.scale.x / oldBaseScale;
		newImg.scale.set(newBaseScale*oldScale);
		newImg.brightness(oldImg.rm);
		float scaleDiff = newImg.scale.y / oldImg.scale.y;
		newImg.x = oldImg.x * scaleDiff;
		newImg.y = oldImg.y * scaleDiff;
		newImg.angle = oldImg.angle;
		return newImg;
	}

	protected void convertArchLayer(ArrayList<Image> layerList, Group layerGroup, float newBaseScale){
		ArrayList<Image> oldImages = new ArrayList<>(layerList);
		layerList.clear();
		for (int i = 0; i < oldImages.size(); i++){
			Image oldArch = oldImages.get(i);
			Image newArch = convertImage(oldArch, newBaseScale);
			layerList.add(newArch);
			layerGroup.add(newArch);
			//if we're at the end of a row and haven't hit the end yet, add more archs!
			while (newArch.x+newArch.width() < width
					&& (i == oldImages.size()-1 || oldImages.get(i+1).y != oldArch.y)){
				Image extraArch = new Image(Assets.Splashes.Title.ARCHS);
				extraArch.frame(getArchFrame());
				extraArch.scale.set(newBaseScale);
				extraArch.x = newArch.x + newArch.width();
				extraArch.x -= 9 * newArch.scale.x; //still want to inset here
				extraArch.y = newArch.y; //y inset already done
				layerList.add(extraArch);
				layerGroup.add(extraArch);
				newArch = extraArch;
			}
		}
	}

	protected void convertFloatingLayer(ArrayList<Image> layerList, Group layerGroup, float newBaseScale, float oldWidth){
		ArrayList<Image> oldImages = new ArrayList<>(layerList);
		layerList.clear();

		float xShift = (width()/newBaseScale)/oldWidth;
		for (int i = 0; i < oldImages.size(); i++){
			Image oldImage = oldImages.get(i);
			Image newImage = convertImage(oldImage, newBaseScale);
			if (newImage.x > 0) {
				newImage.x *= xShift;
			}
			layerList.add(newImage);
			layerGroup.add(newImage);
		}
	}

	ArrayList<Image> toMove = new ArrayList<>();

	@Override
	public synchronized void update() {
		super.update();

		float scale = height / 450f;
		float shift = Game.elapsed * SCROLL_SPEED * scale;

		if (width <= height){
			shift /= 1.5f;
		}
		updateArchLayer(scale, shift);
		if (width <= height){
			scale /= 1.5f;
		}
		shift *= 1.33f;
		updateClusterFarLayer(scale, shift);
		shift *= 1.5f;
		updateClusterLayer(scale, shift);
		shift *= 1.33f;
		updateFarSmallLayer(scale, shift);
		shift *= 1.33f;
		updateMid1Layer(scale, shift);
		shift *= 1.33f;
		updateMid2Layer(scale, shift);
		shift *= 1.33f;
		updateFrontSmallLayer(scale, shift);

	}

	//*** Arch layer logic ***

	private static final float[] INIT_ARCH_CHANCES = {5, 5, 2, 2, 2, 2};
	private static float[] arch_chances = INIT_ARCH_CHANCES.clone();

	public RectF getArchFrame(){

		int tile = Random.chances(arch_chances);
		if (tile == -1){
			arch_chances = INIT_ARCH_CHANCES.clone();
			tile = Random.chances(arch_chances);
		}
		arch_chances[tile]--;

		return ARCH_FILM.get(tile);
	}

	private void updateArchLayer(float scale, float shift){
		float bottom = 0;

		//pass over existing archs, raise them
		for (Image arch : archs){
			arch.y -= shift;
			if (arch.y + arch.height() < 0){
				toMove.add(arch);
			} else if (arch.y + arch.height() > bottom){
				bottom = arch.y + arch.height();
			}
		}

		//move any archs that scrolled off camera to the bottom
		if (!toMove.isEmpty()){
			//we know it'll be one row
			for (Image arch : toMove){
				arch.frame(getArchFrame());
				arch.y = bottom - 5*scale;
			}
			bottom += 100*scale; //arch height
			toMove.clear();
		}

		//if we aren't low enough, add more arch layers
		while (bottom < height){
			float left = -5 + (-33.334f * Random.Int(1, 9) * scale);
			while (left < width){
				Image arch = new Image(Assets.Splashes.Title.ARCHS);
				arch.frame(getArchFrame());
				arch.scale.set(scale);
				arch.x = left;
				arch.y = bottom - 5*scale;
				archLayer.add(arch);
				archs.add(arch);
				left += arch.width() - (9 * scale);
			}
			bottom += 100*scale; //arch height
		}

	}

	//*** Cluster layer logic ***

	private static final float[] INIT_CLUSTER_CHANCES = {2, 2};
	private static float[] cluster_chances = INIT_CLUSTER_CHANCES.clone();

	public RectF getClusterFrame(){

		int tile = Random.chances(cluster_chances);
		if (tile == -1){
			cluster_chances = INIT_CLUSTER_CHANCES.clone();
			tile = Random.chances(cluster_chances);
		}
		cluster_chances[tile]--;

		return CLUSTER_FILM.get(tile);
	}

	private void updateClusterFarLayer(float scale, float shift){
		float bottom = 0;
		float lastX = 0;

		for (Image cluster : clustersFar){
			cluster.y -= shift;
			if (cluster.y + cluster.height() < -20){
				toMove.add(cluster);
			} else if (cluster.y + cluster.height() > bottom){
				bottom = cluster.y + cluster.height();
				lastX = cluster.x;
			}
		}

		if (!toMove.isEmpty()){
			for (Image cluster : toMove){
				cluster.frame(getClusterFrame());
				float flex = 0;
				do {
					cluster.x = Random.Float(-cluster.width()/3f, width - 2*cluster.width()/3f);
					flex += 1;
				} while (Math.abs(cluster.x - lastX) < density*(cluster.width()/2f - flex));
				cluster.y = bottom - cluster.height() + Random.Float(cluster.height()/2f, cluster.height())/density;
				cluster.angle = Random.Float(-20, 20);
				bottom = cluster.y + cluster.height();
				lastX = cluster.x;
			}
			toMove.clear();
		}

		//clusters are 250 tall, add a bit for safety
		float padding = 300 - (300/2f / density);
		while (bottom < (height + padding)){
			Image cluster = new Image(Assets.Splashes.Title.BACK_CLUSTERS);
			cluster.frame(getClusterFrame());
			cluster.scale.set(scale * 0.5f);

			float flex = 0;
			do {
				cluster.x = Random.Float(-cluster.width()/3f, width - 2*cluster.width()/3f);
				flex += 1;
			} while (Math.abs(cluster.x - lastX) < density*(cluster.width()/2f - flex));
			cluster.y = bottom - cluster.height() + Random.Float(cluster.height()/2f, cluster.height())/density;
			cluster.angle = Random.Float(-20, 20);

			cluster.brightness(0.5f);

			clustersFar.add(cluster);
			clustersFarLayer.add(cluster);

			bottom = cluster.y + cluster.height();
			lastX = cluster.x;
		}
	}

	private void updateClusterLayer(float scale, float shift){
		float bottom = 0;
		float lastX = 0;

		for (Image cluster : clusters){
			cluster.y -= shift;
			if (cluster.y + cluster.height() < -20){
				toMove.add(cluster);
			} else if (cluster.y + cluster.height() > bottom){
				bottom = cluster.y + cluster.height();
				lastX = cluster.x;
			}
		}

		if (!toMove.isEmpty()){
			for (Image cluster : toMove){
				cluster.frame(getClusterFrame());
				float flex = 0;
				do {
					cluster.x = Random.Float(-cluster.width()/3f, width - 2*cluster.width()/3f);
					flex += 1;
				} while (Math.abs(cluster.x - lastX) < density*(cluster.width()/2f - flex));
				cluster.y = bottom - cluster.height() + Random.Float(cluster.height()/2f, cluster.height())/density;
				cluster.angle = Random.Float(-20, 20);
				bottom = cluster.y + cluster.height();
				lastX = cluster.x;
			}
			toMove.clear();
		}

		//clusters are 250 tall, add a bit for safety
		float padding = 300 - (300/2f / density);
		while (bottom < (height + padding)){
			Image cluster = new Image(Assets.Splashes.Title.BACK_CLUSTERS);
			cluster.frame(getClusterFrame());
			cluster.scale.set(scale);

			float flex = 0;
			do {
				cluster.x = Random.Float(-cluster.width()/3f, width - 2*cluster.width()/3f);
				flex += 1;
			} while (Math.abs(cluster.x - lastX) < density*(cluster.width()/2f - flex));
			cluster.y = bottom - cluster.height() + Random.Float(cluster.height()/2f, cluster.height())/density;
			cluster.angle = Random.Float(-20, 20);

			cluster.brightness(0.75f);

			clusters.add(cluster);
			clusterLayer.add(cluster);

			bottom = cluster.y + cluster.height();
			lastX = cluster.x;
		}
	}

	//*** Mid layer (1 and 2) logic ***

	private static final float[] INIT_MID_CHANCES = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
	private static float[] mid_chances = INIT_MID_CHANCES.clone();

	private static ArrayList<Integer> lastMids = new ArrayList<>();

	public RectF getMidFrame(){

		int tile = -1;
		do {
			tile = Random.chances(mid_chances);
			if (tile == -1) {
				mid_chances = INIT_MID_CHANCES.clone();
				tile = Random.chances(mid_chances);
			}
		} while (lastMids.contains(tile));
		mid_chances[tile]--;

		lastMids.add(0, tile);
		if (lastMids.size() >= 20){
			lastMids.remove(19);
		}

		return MID_FILM.get(tile);
	}

	private void updateMid1Layer(float scale, float shift){
		float bottom = 0;
		float lastX = 0;

		for (Image mid : mids1){
			mid.y -= shift;
			if (mid.y + mid.height() < -20){
				toMove.add(mid);
			} else if (mid.y + mid.height() > bottom){
				bottom = mid.y + mid.height();
				lastX = mid.x;
			}
		}

		if (!toMove.isEmpty()){
			for (Image mid : toMove){
				mid.frame(getMidFrame());
				mid.scale.set(scale * Random.Float(0.75f, 1.25f));
				float flex = 0;
				do {
					mid.x = Random.Float(-mid.width()/3f, width - 2*mid.width()/3f);
					flex += 1;
				} while (Math.abs(mid.x - lastX) < density*(mid.width()*0.75f - flex));
				mid.y = bottom - mid.height() + Random.Float(mid.height()*0.75f, mid.height())/density;
				mid.angle = Random.Float(-20, 20);
				bottom = mid.y + mid.height();
				lastX = mid.x;
			}
			toMove.clear();
		}

		//mids are ~250 tall, add a bit for safety
		float padding = 300 - (300/2f / density);
		while (bottom < (height + padding)){
			Image mid = new Image(Assets.Splashes.Title.MID_MIXED);
			mid.frame(getMidFrame());
			mid.scale.set(scale * Random.Float(0.75f, 1.25f));
			mid.brightness(0.9f);

			float flex = 0;
			do {
				mid.x = Random.Float(-mid.width()/3f, width - 2*mid.width()/3f);
				flex += 1;
			} while (Math.abs(mid.x - lastX) < density*(mid.width()*0.75f - flex));
			mid.y = bottom - mid.height() + Random.Float(mid.height()/2f, mid.height())/density;
			mid.angle = Random.Float(-20, 20);

			mids1.add(mid);
			mids1Layer.add(mid);

			bottom = mid.y + mid.height();
			lastX = mid.x;
		}
	}

	private void updateMid2Layer(float scale, float shift){
		float bottom = 0;
		float lastX = 0;

		for (Image mid : mids2){
			mid.y -= shift;
			if (mid.y + mid.height() < -20){
				toMove.add(mid);
			} else if (mid.y + mid.height() > bottom){
				bottom = mid.y + mid.height();
				lastX = mid.x;
			}
		}

		if (!toMove.isEmpty()){
			for (Image mid : toMove){
				mid.frame(getMidFrame());
				mid.scale.set(scale * Random.Float(1.25f, 1.75f));
				float flex = 0;
				do {
					mid.x = Random.Float(-mid.width()/3f, width - 2*mid.width()/3f);
					flex += 1;
				} while (Math.abs(mid.x - lastX) < density*(mid.width()*0.75f - flex));
				mid.y = bottom - mid.height() + Random.Float(mid.height()/2f, mid.height())/density;
				mid.angle = Random.Float(-20, 20);
				bottom = mid.y + mid.height();
				lastX = mid.x;
			}
			toMove.clear();
		}

		//mids are ~250 tall, add a bit for safety
		float padding = 300 - (300/2f / density);
		while (bottom < (height + padding)){
			Image mid = new Image(Assets.Splashes.Title.MID_MIXED);
			mid.frame(getMidFrame());
			mid.scale.set(scale * Random.Float(1.25f, 1.75f));

			float flex = 0;
			do {
				mid.x = Random.Float(-mid.width()/3f, width - 2*mid.width()/3f);
				flex += 1;
			} while (Math.abs(mid.x - lastX) < density*(mid.width()*0.75f - flex));
			mid.y = bottom - mid.height() + Random.Float(mid.height()/2f, mid.height())/density;
			mid.angle = Random.Float(-20, 20);

			mids2.add(mid);
			mids2Layer.add(mid);

			bottom = mid.y + mid.height();
			lastX = mid.x;
		}
	}

	//*** Small front layer logic ***

	private static final float[] INIT_SMALL_CHANCES = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
	private static float[] small_chances = INIT_SMALL_CHANCES.clone();

	private static ArrayList<Integer> lastSmalls = new ArrayList<>();

	public RectF getSmallFrame(){

		int tile = -1;
		do {
			tile = Random.chances(small_chances);
			if (tile == -1) {
				small_chances = INIT_SMALL_CHANCES.clone();
				tile = Random.chances(small_chances);
			}
		} while (lastSmalls.contains(tile));
		small_chances[tile]--;

		lastSmalls.add(0, tile);
		if (lastSmalls.size() >= 15){
			lastSmalls.remove(14);
		}

		return SMALL_FILM.get(tile);
	}

	private void updateFarSmallLayer(float scale, float shift) {
		float bottom = 0;
		float lastX = 0;

		for (Image small : smallFars) {
			small.y -= shift;
			if (small.y + small.height() < -20) {
				toMove.add(small);
			} else if (small.y + small.height() > bottom) {
				bottom = small.y + small.height();
				lastX = small.x;
			}
		}

		if (!toMove.isEmpty()) {
			for (Image small : toMove) {
				small.frame(getSmallFrame());
				small.scale.set(scale * Random.Float(0.75f, 1.25f));
				float flex = 0;
				do {
					small.x = Random.Float(small.width() / 3f, width - 4 * small.width() / 3f);
					flex += 1;
				} while (Math.abs(small.x - lastX) < density * (small.width() - flex));
				small.y = bottom - small.height()/2f + Random.Float(small.height() / 2f, small.height()) / density;
				small.angle = Random.Float(-20, 20);
				bottom = small.y + small.height();
				lastX = small.x;
			}
			toMove.clear();
		}

		//smalls are ~115 tall, add a bit for safety
		float padding = 150 - (150 / 2f / density);
		while (bottom < (height + padding)) {
			Image small = new Image(Assets.Splashes.Title.FRONT_SMALL);
			small.frame(getSmallFrame());
			small.scale.set(scale * Random.Float(0.75f, 1.25f));
			small.brightness(0.8f);

			float flex = 0;
			do {
				small.x = Random.Float(small.width() / 3f, width - 4 * small.width() / 3f);
				flex += 1;
			} while (Math.abs(small.x - lastX) < density * (small.width() - flex));
			small.y = bottom - small.height()/2f + Random.Float(small.height() / 2f, small.height()) / density;
			small.angle = Random.Float(-20, 20);

			smallFars.add(small);
			smallFarLayer.add(small);

			bottom = small.y + small.height();
			lastX = small.x;
		}
	}

	private void updateFrontSmallLayer(float scale, float shift){
		float bottom = 0;
		float lastX = 0;

		for (Image small : smallCloses){
			small.y -= shift;
			if (small.y + small.height() < -20){
				toMove.add(small);
			} else if (small.y + small.height() > bottom){
				bottom = small.y + small.height();
				lastX = small.x;
			}
		}

		if (!toMove.isEmpty()){
			for (Image small : toMove){
				small.frame(getSmallFrame());
				small.scale.set(scale * Random.Float(2f, 2.5f));
				float flex = 0;
				do {
					small.x = Random.Float(-small.width()/3f, width - 2*small.width()/3f);
					flex += 1;
				} while (Math.abs(small.x - lastX) < density*(small.width() - flex));
				small.y = bottom - small.height() + Random.Float(small.height()/2f, small.height())/density;
				small.angle = Random.Float(-20, 20);
				bottom = small.y + small.height();
				lastX = small.x;
			}
			toMove.clear();
		}

		//smalls are ~115 tall, add a bit for safety
		float padding = 150 - (150/2f / density);
		while (bottom < (height + padding)){
			Image small = new Image(Assets.Splashes.Title.FRONT_SMALL);
			small.frame(getSmallFrame());
			small.scale.set(scale * Random.Float(2f, 2.5f));

			float flex = 0;
			do {
				small.x = Random.Float(-small.width()/3f, width - 2*small.width()/3f);
				flex += 1;
			} while (Math.abs(small.x - lastX) < density*(small.width() - flex));
			small.y = bottom - small.height() + Random.Float(small.height()/2f, small.height())/density;
			small.angle = Random.Float(-20, 20);

			smallCloses.add(small);
			smallCloseLayer.add(small);

			bottom = small.y + small.height();
			lastX = small.x;
		}
	}

}
