package uk.co.grahamcox.mud.server.telnet.ui.renderer.standard

import org.slf4j.LoggerFactory
import org.springframework.context.ApplicationContext
import uk.co.grahamcox.mud.server.telnet.ui.renderer.Renderer

/**
 * The standard renderer
 * @property renderers The map of renderers factories to use
 * @property stateChanger The state changer to listen to
 */
class StandardRenderer(private val applicationContext: ApplicationContext,
                       private val stateChanger: StateChanger) : Renderer {
    /** The logger to use */
    private val LOG = LoggerFactory.getLogger(StandardRenderer::class.java)

    /** The state the the renderer is currently in */
    private lateinit var state: RendererState

    /** Map of the mapping between state names and Spring bean names */
    private lateinit var stateBeanNames: Map<String, String>

    /** The mechanism to process received bytes */
    private val inputHandler = InputHandler()

    init {
        val stateBeans = applicationContext.getBeanNamesForAnnotation(StateName::class.java)
        LOG.trace("Gathered al of the bean names that can act as states")
        stateBeanNames = stateBeans.map { bean -> bean to applicationContext.getType(bean) }
                .map { bean -> bean.first to bean.second.getAnnotation(StateName::class.java) }
                .map { bean -> bean.first to bean.second.value }
                .map { bean -> bean.second to bean.first }
                .toMap()

        LOG.debug("Initialised state bean mappings: {}", stateBeanNames)

        stateChanger.registerStateChangeListener { newState -> this.changeState(newState) }

        changeState("initial")
    }

    /**
     * Handle when we've received a byte form the client
     * @param byte the byte to handle
     */
    override fun receiveByte(byte: Byte) {
        inputHandler.receiveByte(byte)
    }

    /**
     * Actually change the state that we are in
     * @param newState The new state to change into
     */
    private fun changeState(newState: String) {
        state = createState(newState)
    }

    /**
     * Create the state with the given name
     * @param name The name of the state
     * @return the state
     */
    private fun createState(name: String): RendererState {
        val beanName = stateBeanNames.get(name) ?: throw IllegalArgumentException("Unable to find bean name for state ${name}")
        val newState = applicationContext.getBean(beanName, RendererState::class.java)

        LOG.debug("Created state object {} for state {}", newState, name)
        return newState
    }
}
