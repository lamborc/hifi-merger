////////////////////////////////////////////////////////////////////
//							_ooOoo_                               //
//						   o8888888o                              //
//						   88" . "88                              //
//						   (| ^_^ |)                              //
//						   O\  =  /O                              //
//						____/`---'\____                           //
//					  .'  \\|     |//  `.                         //
//					 /  \\|||  :  |||//  \                        //
//				    /  _||||| -:- |||||-  \                       //
//				    |   | \\\  -  /// |   |                       //
//					| \_|  ''\---/''  |   |                       //
//					\  .-\__  `-`  ___/-. /                       //
//				  ___`. .'  /--.--\  `. . ___                     //
//				."" '<  `.___\_<|>_/___.'  >'"".                  //
//			  | | :  `- \`.;`\ _ /`;.`/ - ` : | |                 //
//		      \  \ `-.   \_ __\ /__ _/   .-` /  /                 //
//		========`-.____`-.___\_____/___.-`____.-'========         //
//							 `=---='                              //
//		^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^        //
//		佛祖保佑             永无BUG   	        永不修改          //
// Copyright© 2018-2020 lambor, Inc.All rights reseved.           //
////////////////////////////////////////////////////////////////////
package com.github.lambor;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.io.SequenceInputStream;
import java.util.ArrayList;
import java.util.List;

public class WaveAudioMerge {

    public final static String AUDIO_TYPE = ".wav";
    public final static String FILE_SEPARATOR = System.getProperty("file.separator");

    private String workRoot;
    public WaveAudioMerge(String workPath){
        this.workRoot = workPath.trim();
        if(this.workRoot!=null && !this.workRoot.equals("")){
            this.workRoot = this.getWorkRoot().replace("/",FILE_SEPARATOR).replace("\\",FILE_SEPARATOR);
        }
        if(!this.workRoot.endsWith(FILE_SEPARATOR)){
            this.workRoot = this.getWorkRoot()+FILE_SEPARATOR;
        }
    }

    public String getWorkRoot() {
        return workRoot;
    }

    public void setWorkRoot(String workRoot) {
        this.workRoot = workRoot;
    }

    /**
     *
     * @param
     * @return
     */
    public File getOutFile(){
        String name = System.currentTimeMillis() + "";
        File outDist = new File(this.getWorkRoot()+"out");
        if(!outDist.exists())outDist.mkdirs();
        File fiOut = new File(outDist,name + AUDIO_TYPE);
        return fiOut;
    }

    public List<File> findFiles(){
        List<File> lst = new ArrayList<>();
        File root = new File(this.getWorkRoot()+"src");
        if(!root.exists()||!root.isDirectory())return lst;

        File[] files = root.listFiles();

        for(File f: files){
            if(f.getName().toLowerCase().endsWith(AUDIO_TYPE)){
                lst.add(f);
            }
        }
        return lst;
    }

    public boolean checkedFormat(List<File> files){
        AudioFormat firstFormat = null;
        if(files==null||files.size()<2)return false;
        try {
            for(File f : files){
                AudioInputStream ais = AudioSystem.getAudioInputStream(f);
                logConsole(ais.getFormat().toString());
                if(firstFormat==null){
                    firstFormat = ais.getFormat();
                }else {
                    boolean b = firstFormat.matches(ais.getFormat());
                    if(!b)return false;
                }
            }
        }catch (UnsupportedAudioFileException| IOException e){
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }

    private void logConsole(String msg){
        System.out.println(">>>>>>"+msg);
    }

    public void merge(List<File> files,File outFile){
        if(files==null||files.size()<2||outFile==null)return;
        try {

            AudioInputStream audio1 = AudioSystem.getAudioInputStream(files.get(0));
            AudioFormat audioFormat = audio1.getFormat();
            AudioInputStream audio2 = AudioSystem.getAudioInputStream(files.get(1));

            AudioInputStream build = new AudioInputStream(
                    new SequenceInputStream(audio1,audio2),
                    audioFormat,
                    audio1.getFrameLength()+audio2.getFrameLength()
            );

            AudioInputStream nextAudio;
            for(int i = 2;i<files.size();i++){
                nextAudio = AudioSystem.getAudioInputStream(files.get(i));
                build = new AudioInputStream(
                        new SequenceInputStream(build,nextAudio),
                        audioFormat,
                        build.getFrameLength()+nextAudio.getFrameLength()
                );
            }

            AudioSystem.write(build, AudioFileFormat.Type.WAVE,outFile);
        }catch (UnsupportedAudioFileException|IOException e){
            logConsole(e.getMessage());
        }
    }
}
