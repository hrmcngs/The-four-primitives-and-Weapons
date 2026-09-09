import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.ImageIO;

/** Java 17 codec/font boundary only. Pixel processing belongs to Common Lisp. */
class ImageBridge {
    public static void main(String[] args) throws Exception {
        if(args.length!=3)throw new IllegalArgumentException("decode|encode|glyphs INPUT OUTPUT");
        BufferedImage image;
        if(args[0].equals("encode")) {
            try(var in=new DataInputStream(new FileInputStream(args[1]))) {
                int w=in.readInt(),h=in.readInt();
                if(w<1||h<1||(long)w*h>16777216)throw new IOException("Invalid dimensions");
                image=new BufferedImage(w,h,BufferedImage.TYPE_INT_ARGB);
                for(int y=0;y<h;y++)for(int x=0;x<w;x++) {
                    int r=in.readUnsignedByte(),g=in.readUnsignedByte(),b=in.readUnsignedByte(),a=in.readUnsignedByte();
                    image.setRGB(x,y,(a<<24)|(r<<16)|(g<<8)|b);
                }
            }
            if(!ImageIO.write(image,"png",new File(args[2])))throw new IOException("PNG unavailable");
            return;
        } else if(args[0].equals("decode")) {
            image=ImageIO.read(new File(args[1]));
            if(image==null)throw new IOException("Unsupported image");
        } else if(args[0].equals("glyphs")) {
            image=new BufferedImage(256,96,BufferedImage.TYPE_INT_ARGB);
            Graphics2D g=image.createGraphics();
            Font font=args[1].equals("default")?new Font(Font.MONOSPACED,Font.PLAIN,12):Font.createFont(Font.TRUETYPE_FONT,new File(args[1])).deriveFont(12f);
            g.setFont(font);g.setColor(Color.WHITE);
            g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
            FontMetrics fm=g.getFontMetrics();
            for(int i=1;i<95;i++) {
                String text=String.valueOf((char)(32+i));
                var gv=font.createGlyphVector(g.getFontRenderContext(),text);
                Rectangle box=gv.getPixelBounds(null,0,0);
                g.drawString(text,(i%16)*16+(16-box.width)/2-box.x,(i/16)*16+(16-box.height)/2-box.y);
            }
            g.dispose();
        } else throw new IllegalArgumentException("Unknown operation");
        try(var out=new DataOutputStream(new FileOutputStream(args[2]))) {
            out.writeInt(image.getWidth());out.writeInt(image.getHeight());
            for(int y=0;y<image.getHeight();y++)for(int x=0;x<image.getWidth();x++) {
                int p=image.getRGB(x,y);out.writeByte(p>>16);out.writeByte(p>>8);out.writeByte(p);out.writeByte(p>>24);
            }
        }
    }
}
