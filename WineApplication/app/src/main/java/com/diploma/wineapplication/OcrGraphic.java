/*
 * Copyright (C) The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.diploma.wineapplication;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;


import java.util.List;

import com.diploma.wineapplication.ui.camera.GraphicOverlay;
import com.google.android.gms.vision.text.Text;
import com.google.android.gms.vision.text.TextBlock;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;


//import java.util.List;

/**
 * Graphic instance for rendering TextBlock position, size, and ID within an associated graphic
 * overlay view.
 */
public class OcrGraphic extends GraphicOverlay.Graphic {
    private static int cnt = 0;
    private static int j = -1;
    private static int i = 0;
    private int id;
    private FirebaseDatabase database;
    private DatabaseReference reference;
    private static final int TEXT_COLOR = Color.WHITE;
    private static String m = "";
    private static String name = "";
    private static Paint rectPaint;
    private static Paint textPaint;
    private final TextBlock textBlock;

    OcrGraphic(GraphicOverlay overlay, TextBlock text) {
        super(overlay);
        database=FirebaseDatabase.getInstance();
        reference=database.getReference();
        textBlock = text;
        if (rectPaint == null) {
            rectPaint = new Paint();
            rectPaint.setColor(TEXT_COLOR);
            rectPaint.setStyle(Paint.Style.STROKE);
            rectPaint.setStrokeWidth(4.0f);
        }

        if (textPaint == null) {
            textPaint = new Paint();
            textPaint.setColor(TEXT_COLOR);
            textPaint.setTextSize(54.0f);
        }
        // Redraw the overlay, as this graphic has been added.
        postInvalidate();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTextBlock() {
        return name;
    }

    /**
     * Checks whether a point is within the bounding box of this graphic.
     * The provided point should be relative to this graphic's containing overlay.
     * @param x An x parameter in the relative context of the canvas.
     * @param y A y parameter in the relative context of the canvas.
     * @return True if the provided point is contained within this graphic's bounding box.
     */
    public boolean contains(float x, float y) {
        if (textBlock == null) {
            return false;
        }
        RectF rect = new RectF(textBlock.getBoundingBox());
        rect = translateRect(rect);
        return rect.contains(x, y);
    }


    /**
     * Draws the text block annotations for position, size, and raw value on the supplied canvas.
     */
    @Override
    public void draw(final Canvas canvas) {
        if (textBlock == null) {
            m = "";
            name = "";
            return;
        }

        // Draws the bounding box around the TextBlock.
        if (!m.equals("")) {
            //RectF rect = new RectF(currentText.getBoundingBox());
            //rect = translateRect(rect);
            //canvas.drawRect(10, 100, 950, 680, rectPaint);
            //TextPaint textPaint = new TextPaint();
            //textPaint.setAntiAlias(true);
            String s[] = m.split("\n");
            for (String cur:s) {
                i = i + 1;
                float left = translateX(150 );
                float bottom = translateY(100 + + 50*i );
                canvas.drawText(cur.toUpperCase(), left, bottom, textPaint);


                //TextPaint mTextPaint=new TextPaint();
                //mTextPaint.setTextSize(40);
                //StaticLayout mTextLayout = new StaticLayout(cur.toUpperCase(), mTextPaint, canvas.getWidth(), Layout.Alignment.ALIGN_NORMAL, 1.0f, 4.0f, false);

                //canvas.save();
// calculate x and y position where your text will be placed


                //canvas.translate(left, bottom);
                //mTextLayout.draw(canvas);
                //canvas.restore();

            }
            i = 0;
            //StaticLayout staticLayout = new StaticLayout(m, textPaint,  250, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0, false);
            //staticLayout.draw(canvas);


        }

        // Break the text into multiple lines and draw each one according to its own bounding box.
        List<? extends Text> textComponents = textBlock.getComponents();
        for(final Text currentText : textComponents) {

            Query QueryRef = reference.child("WineCatalog/");
            QueryRef.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    LentaModel model = dataSnapshot.getValue(LentaModel.class);
                    if(currentText.getValue().toUpperCase().indexOf(model.Original_name.toUpperCase()) >= 0){
                        //RectF rect = new RectF(currentText.getBoundingBox());
                        //rect = translateRect(rect);
                        //canvas.drawRect(rect, rectPaint);
                        //Log.d("language", "Text detected! " + language);
                        //if (language == Language.ENGLISH) {
                        //    String text = translator.translate(currentText.getValue(),Language.ENGLISH, Language.RUSSIAN);
                        //    canvas.drawText(text.toUpperCase(), left, bottom, textPaint);
                        //}
                        //else

                        m = model.Description;
                        name = model.Original_name;
                        cnt = 0;
                    }else
                        cnt = cnt + 1;
                    if (cnt == 1000){
                        m = "";
                        name = "";
                        cnt = 0;
                    }

                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            //Log.d("currentText", "Text detected! " + currentText.getValue());
            //if (currentText.getValue() != null)
            //Translator translator = Translator.getInstance();
            //String language = translator.detect("Hello World");
            //Log.d("language", "Text detected! " + language);
            //float left = translateX(currentText.getBoundingBox().left);
            //float bottom = translateY(currentText.getBoundingBox().bottom);
            //RectF rect = new RectF(currentText.getBoundingBox());
            //rect = translateRect(rect);
            //canvas.drawRect(rect, rectPaint);
            //Log.d("language", "Text detected! " + language);
            //if (language == Language.ENGLISH) {
            //    String text = translator.translate(currentText.getValue(),Language.ENGLISH, Language.RUSSIAN);
            //    canvas.drawText(text.toUpperCase(), left, bottom, textPaint);
            //}
            //else
            //canvas.drawText(currentText.getValue().toUpperCase(), left, bottom, textPaint);
        }
    }
}
