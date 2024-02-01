package gr.forth.ics.isl.syntaxNormalizer;

import java.io.File;

/**
 * Yannis Marketakis (marketak 'at' forth 'dot' ics 'dot' gr)
 */

public interface SyntaxNormalizer {
    public void syntaxNormalize(File sourceFolder, File targetFolder);
}
