import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;
import java.io.*;
import java.util.List;
/*
* 爬虫内容写入
* */
public class BookMypipeline implements Pipeline{
    private String filename;
    //mode=1为待爬取书籍需在章题前加章节数，mode=2则不需要加章节数
    private int mode;
    public BookMypipeline(String filename,int mode){
        this.filename = filename;
        this.mode = mode;
    }
    @Override
    public void process(ResultItems resultItems, Task task) {
        List<String> i = resultItems.get("title");

        if(this.mode == 1) {

            String l = BookPageProcessor.ChapterNum + "." + i.get(0);
            i.clear();
            i.add(l);
        }
            BookPageProcessor.ChapterNum +=1;


        List<String> o = resultItems.get("content");
        try {
            IOWriter(i);
            IOWriter(o);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("共爬取章数："+BookPageProcessor.ChapterNum);

    }
    public void IOWriter(List<String> o) throws IOException {
        try(OutputStream fileOutputStream = new FileOutputStream(filename,true)){
            try(OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream,"UTF-8")){
                try(PrintWriter printWriter = new PrintWriter(outputStreamWriter)){
                    for(String i:o){
                        printWriter.println(i);
                        printWriter.println("");
                    }
                }

            }
        }
    }
}
