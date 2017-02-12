package is.anubis;

import java.beans.PropertyVetoException;
import java.util.Locale;

import javax.speech.AudioException;
import javax.speech.Central;
import javax.speech.EngineException;
import javax.speech.EngineStateError;
import javax.speech.synthesis.Synthesizer;
import javax.speech.synthesis.SynthesizerModeDesc;
import javax.speech.synthesis.Voice;

public class TextToSpeech {
    Synthesizer synthesizer;

    public TextToSpeech() {
        try {
            SynthesizerModeDesc desc = null;
            if ( desc == null ) {
                System.setProperty( "freetts.voices", "com.sun.speech.freetts.en.us.cmu_us_kal.KevinVoiceDirectory" );
                desc = new SynthesizerModeDesc( Locale.US );
                Central.registerEngineCentral( "com.sun.speech.freetts.jsapi.FreeTTSEngineCentral" );
                synthesizer = Central.createSynthesizer( desc );
                synthesizer.allocate();
                synthesizer.resume();
                SynthesizerModeDesc smd = ( SynthesizerModeDesc ) synthesizer.getEngineModeDesc();
                Voice[] voices = smd.getVoices();
                Voice voice = null;
                for ( Voice v : voices ) {
                    System.out.println( v.getName() );
                    if ( v.getName().equals( "kevin16" ) ) {
                        voice = v;
                        break;
                    }
                }
                synthesizer.getSynthesizerProperties().setVoice( voice );
            }
        } catch ( EngineException | AudioException | EngineStateError | PropertyVetoException e ) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    public void speak( String text )
    {
        synthesizer.speakPlainText( text, null );
    }
    
    public void dispose()
    {
        try {
            synthesizer.deallocate();
        } catch ( EngineException | EngineStateError e ) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
