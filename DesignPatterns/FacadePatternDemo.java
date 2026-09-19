package DesignPatterns;

/**
 * Demonstrates the Facade Design Pattern (Structural Pattern).
 * 
 * Intent: Provide a unified, simplified interface to a complex subsystem
 * of classes, interfaces, and algorithms. This shields clients from internal
 * complexity, reduces coupling, and adheres to the Principle of Least Knowledge
 * (Law of Demeter).
 * 
 * Included Examples:
 * 1. Multimedia Video Conversion Pipeline: Orchestrates audio/video decoders,
 *    bitrate compressors, and audio mixing into a one-line API.
 * 2. Smart Home Entertainment Automation: Coordinates lights, sound system,
 *    projector, and streaming player for movie and music sessions.
 */
public class FacadePatternDemo {

    // =========================================================================
    // Subsystem 1: Video Transcoding and Conversion Subsystem
    // =========================================================================

    public static class VideoFile {
        private final String fileName;
        private final String format;
        private final byte[] data;

        public VideoFile(String fileName) {
            this.fileName = fileName;
            this.format = fileName.contains(".") ? fileName.substring(fileName.lastIndexOf('.') + 1) : "unknown";
            this.data = new byte[1024]; // Simulated raw payload
        }

        public String getFileName() { return fileName; }
        public String getFormat() { return format; }
        public int getSizeKb() { return data.length; }
    }

    public interface Codec {
        String getCodecType();
    }

    public static class MPEG4CompressionCodec implements Codec {
        @Override
        public String getCodecType() { return "mp4"; }
    }

    public static class OggCompressionCodec implements Codec {
        @Override
        public String getCodecType() { return "ogg"; }
    }

    public static class CodecFactory {
        public static Codec extract(VideoFile file) {
            String type = file.getFormat().toLowerCase();
            if (type.equals("mp4")) {
                System.out.println("  [CodecFactory] Extracted MPEG4 codec stream.");
                return new MPEG4CompressionCodec();
            } else {
                System.out.println("  [CodecFactory] Extracted OGG Vorbis codec stream.");
                return new OggCompressionCodec();
            }
        }
    }

    public static class BitrateReader {
        public static byte[] read(VideoFile file, Codec codec) {
            System.out.printf("  [BitrateReader] Reading buffer for '%s' using %s codec.%n",
                    file.getFileName(), codec.getCodecType());
            return new byte[512];
        }

        public static byte[] convert(byte[] buffer, Codec destinationCodec) {
            System.out.printf("  [BitrateReader] Re-encoding byte stream to destination format [%s]...%n",
                    destinationCodec.getCodecType());
            return buffer;
        }
    }

    public static class AudioMixer {
        public void fixAudioFrequencies(byte[] audioTrack) {
            System.out.println("  [AudioMixer] Equalizing frequencies and normalizing stereo channels.");
        }

        public void applyNoiseReduction() {
            System.out.println("  [AudioMixer] Applying noise cancellation filter (-18dB).");
        }
    }

    public static class WatermarkService {
        public void stamp(String label) {
            System.out.printf("  [WatermarkService] Burning semi-transparent watermark: '%s'%n", label);
        }
    }

    /**
     * Facade: Wraps the complex 5-step video processing pipeline
     * into simple, client-friendly methods.
     */
    public static class VideoConverterFacade {
        private final AudioMixer audioMixer;
        private final WatermarkService watermarkService;

        public VideoConverterFacade() {
            this.audioMixer = new AudioMixer();
            this.watermarkService = new WatermarkService();
        }

        public VideoFile convertVideo(String fileName, String targetFormat, String branding) {
            System.out.printf("%n=== [VideoConverterFacade] Starting conversion of '%s' to '%s' ===%n", fileName, targetFormat);
            VideoFile sourceFile = new VideoFile(fileName);
            Codec sourceCodec = CodecFactory.extract(sourceFile);

            Codec destinationCodec;
            if ("mp4".equalsIgnoreCase(targetFormat)) {
                destinationCodec = new MPEG4CompressionCodec();
            } else {
                destinationCodec = new OggCompressionCodec();
            }

            byte[] rawBuffer = BitrateReader.read(sourceFile, sourceCodec);
            byte[] intermediate = BitrateReader.convert(rawBuffer, destinationCodec);

            audioMixer.applyNoiseReduction();
            audioMixer.fixAudioFrequencies(intermediate);

            if (branding != null && !branding.isBlank()) {
                watermarkService.stamp(branding);
            }

            String destinationFileName = fileName.substring(0, fileName.lastIndexOf('.')) + "." + targetFormat;
            System.out.printf("=== [VideoConverterFacade] Finished! Produced ready artifact '%s' ===%n", destinationFileName);
            return new VideoFile(destinationFileName);
        }
    }

    // =========================================================================
    // Subsystem 2: Smart Home Entertainment Subsystem
    // =========================================================================

    public static class SmartLighting {
        private int brightness = 100;

        public void dim(int percentage) {
            this.brightness = percentage;
            System.out.printf("  [SmartLighting] Lights dimmed to %d%%%n", brightness);
        }

        public void on() {
            this.brightness = 100;
            System.out.println("  [SmartLighting] Lights fully illuminated (100%).");
        }
    }

    public static class SurroundSoundSystem {
        public void powerOn() { System.out.println("  [AudioSystem] Dolby Atmos 7.1 Surround Sound ON."); }
        public void powerOff() { System.out.println("  [AudioSystem] Sound system standby."); }
        public void setVolume(int level) { System.out.printf("  [AudioSystem] Master volume set to %d.%n", level); }
        public void setSurroundMode() { System.out.println("  [AudioSystem] Cinema Surround sound mode enabled."); }
    }

    public static class Projector {
        public void turnOn() { System.out.println("  [Projector] 4K Laser Projector powered ON."); }
        public void turnOff() { System.out.println("  [Projector] Projector cooling down and powering OFF."); }
        public void setWideScreenMode() { System.out.println("  [Projector] Aspect ratio configured to 16:9 cinematic widescreen."); }
    }

    public static class StreamingPlayer {
        public void startApp(String appName) { System.out.printf("  [StreamingPlayer] Launching %s application.%n", appName); }
        public void play(String title) { System.out.printf("  [StreamingPlayer] Streaming '%s' in 4K HDR.%n", title); }
        public void stop() { System.out.println("  [StreamingPlayer] Playback stopped."); }
    }

    public static class PopcornMaker {
        public void turnOn() { System.out.println("  [PopcornMaker] Heating up kettle..."); }
        public void pop() { System.out.println("  [PopcornMaker] Popping delicious hot buttered popcorn!"); }
        public void turnOff() { System.out.println("  [PopcornMaker] Kettle off."); }
    }

    /**
     * Facade: Provides simple one-touch operations for complex home entertainment.
     */
    public static class HomeTheaterFacade {
        private final SmartLighting lighting;
        private final SurroundSoundSystem sound;
        private final Projector projector;
        private final StreamingPlayer player;
        private final PopcornMaker popper;

        public HomeTheaterFacade(SmartLighting lighting, SurroundSoundSystem sound,
                                 Projector projector, StreamingPlayer player, PopcornMaker popper) {
            this.lighting = lighting;
            this.sound = sound;
            this.projector = projector;
            this.player = player;
            this.popper = popper;
        }

        public void watchMovie(String movieTitle) {
            System.out.printf("%n--- [HomeTheaterFacade] Preparing theater for movie: '%s' ---%n", movieTitle);
            popper.turnOn();
            popper.pop();
            lighting.dim(15);
            projector.turnOn();
            projector.setWideScreenMode();
            sound.powerOn();
            sound.setSurroundMode();
            sound.setVolume(28);
            player.startApp("Netflix");
            player.play(movieTitle);
            System.out.println("--- [HomeTheaterFacade] Enjoy your movie! ---");
        }

        public void endMovie() {
            System.out.println("\n--- [HomeTheaterFacade] Shutting down theater experience ---");
            popper.turnOff();
            player.stop();
            sound.powerOff();
            projector.turnOff();
            lighting.on();
            System.out.println("--- [HomeTheaterFacade] Theater restored to daytime mode ---");
        }
    }

    // =========================================================================
    // Demonstration and Verification
    // =========================================================================

    public static void main(String[] args) {
        System.out.println("=========================================================");
        System.out.println("           FACADE DESIGN PATTERN DEMONSTRATION           ");
        System.out.println("=========================================================");

        // Demo 1: Transcoding complex video via clean Facade
        VideoConverterFacade converterFacade = new VideoConverterFacade();
        VideoFile output1 = converterFacade.convertVideo("intro_cinematic.ogg", "mp4", "(c) 2026 Studio");
        System.out.printf("Client received transcoded artifact: %s (Format: %s)%n",
                output1.getFileName(), output1.getFormat());

        // Demo 2: Smart Home Automation Facade
        SmartLighting lights = new SmartLighting();
        SurroundSoundSystem sound = new SurroundSoundSystem();
        Projector projector = new Projector();
        StreamingPlayer player = new StreamingPlayer();
        PopcornMaker popper = new PopcornMaker();

        HomeTheaterFacade homeTheater = new HomeTheaterFacade(lights, sound, projector, player, popper);
        homeTheater.watchMovie("Interstellar");
        homeTheater.endMovie();

        System.out.println("\n[FacadePatternDemo] Execution finished successfully!");
    }
}
