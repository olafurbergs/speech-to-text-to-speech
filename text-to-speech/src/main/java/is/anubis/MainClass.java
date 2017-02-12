package is.anubis;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Date;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import edu.cmu.sphinx.api.Configuration;
import edu.cmu.sphinx.api.LiveSpeechRecognizer;
import edu.cmu.sphinx.api.SpeechResult;

public class MainClass {

    public MainClass( String text ) {
    }

    static String convertNumbers( String text )
    {
        String ret = text.replace( "one", "1" );
        ret = ret.replace( "two", "2" );
        ret = ret.replace( "three", "3" );
        ret = ret.replace( "four", "4" );
        ret = ret.replace( "five", "5" );
        ret = ret.replace( "six", "6" );
        ret = ret.replace( "seven", "7" );
        ret = ret.replace( "eight", "8" );
        ret = ret.replace( "nine", "9" );
        ret = ret.replace( "ten", "10" );
        ret = ret.replace( "eleven", "11" );
        ret = ret.replace( "twelve", "12" );
        ret = ret.replace( "thirteen", "13" );
        ret = ret.replace( "fourteen", "14" );
        ret = ret.replace( "fifteen", "15" );
        ret = ret.replace( "sixteen", "16" );
        ret = ret.replace( "seventeen", "17" );
        ret = ret.replace( "eighteen", "18" );
        ret = ret.replace( "nineteen", "19" );
        ret = ret.replace( "twenty", "20" );
        ret = ret.replace( "hundred", "100" );
        ret = ret.replace( "thousand", "1000" );
        return ret;
    }
    
    public static void main( String[] args ) {
        TextToSpeech tts = new TextToSpeech();
        try {
            Configuration configuration = new Configuration();

            // Set path to acoustic model.
            configuration.setAcousticModelPath( "resource:/edu/cmu/sphinx/models/en-us/en-us" );
            // Set path to dictionary.
            configuration.setDictionaryPath( "resource:/edu/cmu/sphinx/models/en-us/cmudict-en-us.dict" );
            // Set language model.
            configuration.setLanguageModelPath( "resource:/edu/cmu/sphinx/models/en-us/en-us.lm.bin" );
            LiveSpeechRecognizer recognizer = new LiveSpeechRecognizer( configuration );
            // Start recognition process pruning previously cached data.
            while ( true ) {
                System.out.println( "Start speaking" );
                recognizer.startRecognition( false );
                SpeechResult result = recognizer.getResult();
                recognizer.stopRecognition();
                String recognizedText = result.getHypothesis();
                if ( recognizedText.contains( "what time is it" ) )
                {
                    Date dateobj = new Date();
                    @SuppressWarnings( "deprecation" )
                    String thetime = String.format( "The time is %d %d", dateobj.getHours(), dateobj.getMinutes() );
                    tts.speak( thetime );
                }
                else if ( recognizedText.contains(  "how do i" ) )
                {
                    String google = "http://www.google.com/search?q=";
                    String search = recognizedText;
                    String charset = "UTF-8";
                    String userAgent = "ExampleBot 1.0 (+http://example.com/bot)"; // Change this to your company's name and bot homepage!

                    Elements links = Jsoup.connect(google + URLEncoder.encode(search, charset)).userAgent(userAgent).get().select(".g>.r>a");

                    for (Element link : links) {
                        String title = link.text();
                        String url = link.absUrl("href"); // Google returns URLs in format "http://www.google.com/url?q=<url>&sa=U&ei=<someKey>".
                        url = URLDecoder.decode(url.substring(url.indexOf('=') + 1, url.indexOf('&')), "UTF-8");

                        if (!url.startsWith("http")) {
                            continue; // Ads/news/etc.
                        }

                        tts.speak( title );
                    }
                    
                }
                else if ( recognizedText.contains(  "how old are you" ) )
                {
                    tts.speak( "I am very young, only 1 year old." );
                }   
                else if ( recognizedText.contains(  "what is" ) )
                {
                    String str = convertNumbers( recognizedText ).replaceAll("[^0-9]+", " ");
                    String[] numbers = str.trim().split(" ");
                    for( String s : numbers )
                    {
                        System.err.println( s );
                    }
                    if ( numbers.length < 2 )
                    {
                        tts.speak( "That does not compute!" );
                        System.out.println( result.getHypothesis() );
                        continue;
                    }
                    if ( recognizedText.contains( "plus" ) )
                    {
                        tts.speak( String.format( "The answer is %d", Integer.parseInt( numbers[ 0 ] ) + Integer.parseInt( numbers[ 1 ] ) ) );
                    }
                    else if ( recognizedText.contains( "minus" ) )
                    {
                        tts.speak( String.format( "The answer is %d", Integer.parseInt( numbers[ 0 ] ) - Integer.parseInt( numbers[ 1 ] ) ) );
                    }
                    else if ( recognizedText.contains( "times" ) )
                    {
                        tts.speak( String.format( "The answer is %d", Integer.parseInt( numbers[ 0 ] ) * Integer.parseInt( numbers[ 1 ] ) ) );
                    }
                    else if ( recognizedText.contains( "divided" ) )
                    {
                        tts.speak( String.format( "The answer is %d", Integer.parseInt( numbers[ 0 ] ) / Integer.parseInt( numbers[ 1 ] ) ) );
                    }
                    else
                    {
                        tts.speak( "That does not compute!" );
                    }
                }
                else
                {
                    tts.speak( recognizedText );
                }

                System.out.println( result.getHypothesis() );
                System.out.println( result.getResult().getBestFinalResultNoFiller() );
                System.out.println( result.getResult().getBestPronunciationResult() );
                System.out.println( result.getResult().getBestResultNoFiller() );
            }

        } catch ( Exception e ) {
            e.printStackTrace();
        }

    }
}
