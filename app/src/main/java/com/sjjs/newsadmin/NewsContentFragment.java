package com.sjjs.newsadmin;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.text.Editable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.github.irshulx.Editor;
import com.github.irshulx.EditorListener;
import com.github.irshulx.models.EditorTextStyle;
import com.sjjs.newsadmin.utils.Config;

import java.util.Map;

import jp.wasabeef.richeditor.RichEditor;


public class NewsContentFragment extends Fragment {


//    private RichEditor mEditor;
//    private TextView mPreview;
    Editor editor;
    Button nextButton;


    public NewsContentFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_news_content, container, false);

//        mEditor = (RichEditor) view.findViewById(R.id.editor);
//        mPreview = (TextView) view.findViewById(R.id.preview);

        editor =  view.findViewById(R.id.editor);
        nextButton = view.findViewById(R.id.btn_submit_content);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Config.isNewsItemFormTwoContainsEmpty()){
                    Navigation.findNavController(v).navigate(R.id.action_newsContentFragment_to_newsOtherFragment);
                }
            }
        });

        editor.setEditorListener(new EditorListener() {
            @Override
            public void onTextChanged(EditText editText, Editable text) {
                if (editor.getContent() != null) {
                    Config.newsState.setNewsContent(editor.getContentAsHTML());
                }
            }

            @Override
            public void onUpload(Bitmap image, String uuid) {

            }

            @Override
            public View onRenderMacro(String name, Map<String, Object> props, int index) {
                return null;
            }
        });



        view.findViewById(R.id.action_h1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.updateTextStyle(EditorTextStyle.H1);
            }
        });

        view.findViewById(R.id.action_h2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.updateTextStyle(EditorTextStyle.H2);
            }
        });

        view.findViewById(R.id.action_h3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.updateTextStyle(EditorTextStyle.H3);
            }
        });

        view.findViewById(R.id.action_bold).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.updateTextStyle(EditorTextStyle.BOLD);
            }
        });

        view.findViewById(R.id.action_Italic).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.updateTextStyle(EditorTextStyle.ITALIC);
            }
        });

        /*view.findViewById(R.id.action_indent).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.updateTextStyle(EditorTextStyle.INDENT);
            }
        });*/

        view.findViewById(R.id.action_blockquote).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.updateTextStyle(EditorTextStyle.BLOCKQUOTE);
            }
        });

        /*view.findViewById(R.id.action_outdent).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.updateTextStyle(EditorTextStyle.OUTDENT);
            }
        });*/

        view.findViewById(R.id.action_bulleted).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.insertList(false);
            }
        });

        view.findViewById(R.id.action_unordered_numbered).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.insertList(true);
            }
        });

        /*view.findViewById(R.id.action_hr).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.insertDivider();
            }
        });*/

        editor.render();




//        mEditor.setEditorHeight(200);
//        mEditor.setEditorFontSize(22);
//        mEditor.setEditorFontColor(Color.RED);
//        mEditor.setPadding(10, 10, 10, 10);
//        mEditor.setPlaceholder("Insert text here...");
//
//        mEditor.setOnTextChangeListener(new RichEditor.OnTextChangeListener() {
//            @Override public void onTextChange(String text) {
//                mPreview.setText(text);
//            }
//        });
//
//        view.findViewById(R.id.action_undo).setOnClickListener(new View.OnClickListener() {
//            @Override public void onClick(View v) {
//                mEditor.undo();
//            }
//        });
//
//        view.findViewById(R.id.action_redo).setOnClickListener(new View.OnClickListener() {
//            @Override public void onClick(View v) {
//                mEditor.redo();
//            }
//        });
//
//        view.findViewById(R.id.action_bold).setOnClickListener(new View.OnClickListener() {
//            @Override public void onClick(View v) {
//                mEditor.setBold();
//            }
//        });
//
//        view.findViewById(R.id.action_italic).setOnClickListener(new View.OnClickListener() {
//            @Override public void onClick(View v) {
//                mEditor.setItalic();
//            }
//        });
//
//        view.findViewById(R.id.action_subscript).setOnClickListener(new View.OnClickListener() {
//            @Override public void onClick(View v) {
//                mEditor.setSubscript();
//            }
//        });
//
//        view.findViewById(R.id.action_superscript).setOnClickListener(new View.OnClickListener() {
//            @Override public void onClick(View v) {
//                mEditor.setSuperscript();
//            }
//        });
//
//        view.findViewById(R.id.action_strikethrough).setOnClickListener(new View.OnClickListener() {
//            @Override public void onClick(View v) {
//                mEditor.setStrikeThrough();
//            }
//        });
//
//        view.findViewById(R.id.action_underline).setOnClickListener(new View.OnClickListener() {
//            @Override public void onClick(View v) {
//                mEditor.setUnderline();
//            }
//        });
//
//        view.findViewById(R.id.action_heading1).setOnClickListener(new View.OnClickListener() {
//            @Override public void onClick(View v) {
//                mEditor.setHeading(1);
//            }
//        });
//
//        view.findViewById(R.id.action_heading2).setOnClickListener(new View.OnClickListener() {
//            @Override public void onClick(View v) {
//                mEditor.setHeading(2);
//            }
//        });
//
//        view.findViewById(R.id.action_heading3).setOnClickListener(new View.OnClickListener() {
//            @Override public void onClick(View v) {
//                mEditor.setHeading(3);
//            }
//        });
//
//        view.findViewById(R.id.action_heading4).setOnClickListener(new View.OnClickListener() {
//            @Override public void onClick(View v) {
//                mEditor.setHeading(4);
//            }
//        });
//
//        view.findViewById(R.id.action_heading5).setOnClickListener(new View.OnClickListener() {
//            @Override public void onClick(View v) {
//                mEditor.setHeading(5);
//            }
//        });
//
//        view.findViewById(R.id.action_heading6).setOnClickListener(new View.OnClickListener() {
//            @Override public void onClick(View v) {
//                mEditor.setHeading(6);
//            }
//        });
//
//        view.findViewById(R.id.action_txt_color).setOnClickListener(new View.OnClickListener() {
//            private boolean isChanged;
//
//            @Override public void onClick(View v) {
//                mEditor.setTextColor(isChanged ? Color.BLACK : Color.RED);
//                isChanged = !isChanged;
//            }
//        });
//
//        view.findViewById(R.id.action_bg_color).setOnClickListener(new View.OnClickListener() {
//            private boolean isChanged;
//
//            @Override public void onClick(View v) {
//                mEditor.setTextBackgroundColor(isChanged ? Color.TRANSPARENT : Color.YELLOW);
//                isChanged = !isChanged;
//            }
//        });
//
//        view.findViewById(R.id.action_indent).setOnClickListener(new View.OnClickListener() {
//            @Override public void onClick(View v) {
//                mEditor.setIndent();
//            }
//        });
//
//        view.findViewById(R.id.action_outdent).setOnClickListener(new View.OnClickListener() {
//            @Override public void onClick(View v) {
//                mEditor.setOutdent();
//            }
//        });
//
//        view.findViewById(R.id.action_align_left).setOnClickListener(new View.OnClickListener() {
//            @Override public void onClick(View v) {
//                mEditor.setAlignLeft();
//            }
//        });
//
//        view.findViewById(R.id.action_align_center).setOnClickListener(new View.OnClickListener() {
//            @Override public void onClick(View v) {
//                mEditor.setAlignCenter();
//            }
//        });
//
//        view.findViewById(R.id.action_align_right).setOnClickListener(new View.OnClickListener() {
//            @Override public void onClick(View v) {
//                mEditor.setAlignRight();
//            }
//        });
//
//        view.findViewById(R.id.action_blockquote).setOnClickListener(new View.OnClickListener() {
//            @Override public void onClick(View v) {
//                mEditor.setBlockquote();
//            }
//        });
//
//        view.findViewById(R.id.action_insert_bullets).setOnClickListener(new View.OnClickListener() {
//            @Override public void onClick(View v) {
//                mEditor.setBullets();
//            }
//        });
//
//        view.findViewById(R.id.action_insert_numbers).setOnClickListener(new View.OnClickListener() {
//            @Override public void onClick(View v) {
//                mEditor.setNumbers();
//            }
//        });
//
//        view.findViewById(R.id.action_insert_image).setOnClickListener(new View.OnClickListener() {
//            @Override public void onClick(View v) {
//                mEditor.insertImage("http://www.1honeywan.com/dachshund/image/7.21/7.21_3_thumb.JPG",
//                        "dachshund");
//            }
//        });
//
//        view.findViewById(R.id.action_insert_link).setOnClickListener(new View.OnClickListener() {
//            @Override public void onClick(View v) {
//                mEditor.insertLink("https://github.com/wasabeef", "wasabeef");
//            }
//        });
//        view.findViewById(R.id.action_insert_checkbox).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mEditor.insertTodo();
//            }
//        });


        return view;
    }
}