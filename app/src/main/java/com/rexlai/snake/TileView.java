package com.rexlai.snake;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by rexlai on 2016/5/17.
 */
public class TileView extends View{
    /**
     * Parameters controlling the size of the tiles and their range within view.
     * Width/Height are in pixels, and Drawables will be scaled to fit to these
     * dimensions. X/Y Tile Counts are the number of tiles that will be drawn.
     */

    protected static int tileSize;

    protected static int xTileCount;
    protected static int yTileCount;

    private static int xOffset;
    private static int yOffset;


    /**
     * A hash that maps integer handles specified by the subclasser to the
     * drawable that will be used for that reference
     */
    private Bitmap[] tileArray;

    /**
     * A two-dimensional array of integers in which the number represents the
     * index of the tile that should be drawn at that locations
     */
    private int[][] tileGrid;

    private final Paint paint = new Paint();

    public TileView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TileView);

        tileSize = a.getInt(R.styleable.TileView_tileSize, 36);

        a.recycle();
    }

    public TileView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TileView);

        tileSize = a.getInt(R.styleable.TileView_tileSize, 36);

        a.recycle();
    }



    /**
     * Rests the internal array of Bitmaps used for drawing tiles, and
     * sets the maximum index of tiles to be inserted
     *
     * @param tilecount
     */

    public void resetTiles(int tilecount) {
        tileArray = new Bitmap[tilecount];
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        xTileCount = (int) Math.floor(w / tileSize);
        yTileCount = (int) Math.floor(h / tileSize);

        xOffset = ((w - (tileSize * xTileCount)) / 2);
        yOffset = ((h - (tileSize * yTileCount)) / 2);

        tileGrid = new int[xTileCount][yTileCount];
        clearTiles();
    }

    /**
     * Function to set the specified Drawable as the tile for a particular
     * integer key.
     *
     * @param key
     * @param tile
     */
    public void loadTile(int key, Drawable tile) {
        Bitmap bitmap = Bitmap.createBitmap(tileSize, tileSize, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        tile.setBounds(0, 0, tileSize, tileSize);
        tile.draw(canvas);

        tileArray[key] = bitmap;
    }

    /**
     * Resets all tiles to 0 (empty)
     *
     */
    public void clearTiles() {
        for (int x = 0; x < xTileCount; x++) {
            for (int y = 0; y < yTileCount; y++) {
                setTile(0, x, y);
            }
        }
    }

    /**
     * Used to indicate that a particular tile (set with loadTile and referenced
     * by an integer) should be drawn at the given x/y coordinates during the
     * next invalidate/draw cycle.
     *
     * @param tileindex
     * @param x
     * @param y
     */
    public void setTile(int tileindex, int x, int y) {
        tileGrid[x][y] = tileindex;
    }


    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        Log.e("debug", "xTileCount:" + xTileCount);
//        Log.e("debug", "yTileCount:" + yTileCount);
        for (int x = 0; x < xTileCount; x += 1) {
            for (int y = 0; y < yTileCount; y += 1) {
                if (tileGrid[x][y] > 0) {
                    canvas.drawBitmap(tileArray[tileGrid[x][y]],
                            xOffset + x * tileSize,
                            yOffset + y * tileSize,
                            paint);
                }
            }
        }

    }
}
