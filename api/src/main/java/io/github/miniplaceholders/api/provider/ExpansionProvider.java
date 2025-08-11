package io.github.miniplaceholders.api.provider;

import io.github.miniplaceholders.api.Expansion;

/**
 * An expansion provider.
 */
public interface ExpansionProvider {
    /**
     * Provides an expansion.<br>
     *
     * @return a expansion
     * @apiNote If used outside its MiniPlaceholders dynamic expansion loading context,
     *          you should always check that the loading requirements specified in {@link #loadRequirement} are met.
     */
    Expansion provideExpansion();

    /**
     * Obtain the requirements that must be met for this expansion to load successfully.
     * @return the load requirements of this expansion provider
     */
    LoadRequirement loadRequirement();
}
