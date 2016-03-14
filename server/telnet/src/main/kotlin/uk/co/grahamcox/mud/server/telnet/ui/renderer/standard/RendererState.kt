package uk.co.grahamcox.mud.server.telnet.ui.renderer.standard

/**
 * Interface describing the current state that the renderer is in.
 * Note that the renderer can only ever be in a single state.
 */
interface RendererState {
}

/**
 * Annotation that must be applied to the state class to indicate the name of the state
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class StateName(val value: String)