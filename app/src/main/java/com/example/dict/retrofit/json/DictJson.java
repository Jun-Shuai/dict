package com.example.dict.retrofit.json;

import java.util.List;

public class DictJson {


    /**
     * flag : 0
     * msg : success
     * data : [{"id":1,"word":"distant","phonogram":"[ˈdistənt]","translate":"adj. 远的；遥远的；疏远的；不亲近的","music":"music/distant.mp3","word_tream":"考研必备词汇"},{"id":2,"word":"treat","phonogram":"[tri:t]","translate":"n. 款待，请客","music":"music/treat.mp3","word_tream":"考研必备词汇"},{"id":3,"word":"anywhere","phonogram":"[ˈeniˌweə(r)]","translate":"adv. 无论哪里；（用于否定、疑问等）任何地方","music":"music/anywhere.mp3","word_tream":"考研必备词汇"},{"id":4,"word":"pattern","phonogram":"[ˈpætən]","translate":"v. 仿制，模仿","music":"music/pattern.mp3","word_tream":"考研必备词汇"}]
     */

    private String flag;
    private String msg;
    private List<DataBean> data;

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 1
         * word : distant
         * phonogram : [ˈdistənt]
         * translate : adj. 远的；遥远的；疏远的；不亲近的
         * music : music/distant.mp3
         * word_tream : 考研必备词汇
         */

        private int id;
        private String word;
        private String phonogram;
        private String translate;
        private String music;
        private String word_tream;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getWord() {
            return word;
        }

        public void setWord(String word) {
            this.word = word;
        }

        public String getPhonogram() {
            return phonogram;
        }

        public void setPhonogram(String phonogram) {
            this.phonogram = phonogram;
        }

        public String getTranslate() {
            return translate;
        }

        public void setTranslate(String translate) {
            this.translate = translate;
        }

        public String getMusic() {
            return music;
        }

        public void setMusic(String music) {
            this.music = music;
        }

        public String getWord_tream() {
            return word_tream;
        }

        public void setWord_tream(String word_tream) {
            this.word_tream = word_tream;
        }

        @Override
        public String toString() {
            return "DataBean{" +
                    "id=" + id +
                    ", word='" + word + '\'' +
                    ", phonogram='" + phonogram + '\'' +
                    ", translate='" + translate + '\'' +
                    ", music='" + music + '\'' +
                    ", word_tream='" + word_tream + '\'' +
                    '}';
        }
    }
}
