package ma.crm.carental.conf;

import org.simpleframework.xml.core.PersistenceException;
import org.simpleframework.xml.core.Persister;
import org.springframework.aot.hint.MemberCategory;
import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;
import org.springframework.aot.hint.TypeReference;


import io.minio.GetPresignedObjectUrlArgs;
import io.minio.UploadSnowballObjectsArgs;
import io.minio.messages.LocationConstraint;

public class HintsConfig implements RuntimeHintsRegistrar{

    @Override
    public void registerHints(RuntimeHints hints, ClassLoader classLoader) {
        hints.resources().registerPattern("my-key\\.pub") ;

        hints.reflection().registerType(UploadSnowballObjectsArgs.class , MemberCategory.values()) ;

        hints.reflection().registerType(GetPresignedObjectUrlArgs.class, MemberCategory.values()) ;

        // Register the LocationConstraint class for reflection
        hints.reflection().registerType(
            LocationConstraint.class,
            MemberCategory.values() // Include all constructors, methods, and fields
        );

        // Register simpleframework.xml classes for reflection
        hints.reflection().registerType(
            PersistenceException.class,
            MemberCategory.values()
        );

        hints.reflection().registerType(
            Persister.class,
            MemberCategory.values()
        );

        // Register the TextLabel class for reflection (using fully qualified name)
        hints.reflection().registerType(
            TypeReference.of("org.simpleframework.xml.core.TextLabel"),
            MemberCategory.values() // Include all constructors, methods, and fields
        );

        // Register the Contact class for reflection (using fully qualified name)
        hints.reflection().registerType(
            TypeReference.of("org.simpleframework.xml.core.Contact"),
            MemberCategory.values()
        );

        // Register the Text annotation for reflection (using fully qualified name)
        hints.reflection().registerType(
            TypeReference.of("org.simpleframework.xml.Text"),
            MemberCategory.values()
        );

        // Register the Format class for reflection (using fully qualified name)
        hints.reflection().registerType(
            TypeReference.of("org.simpleframework.xml.stream.Format"),
            MemberCategory.values()
        );
    }
    
}
