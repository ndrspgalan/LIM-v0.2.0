package domain.social;

import java.util.*;

/**
 * – — Personas, vida cotidiana, poder y élite epistemológica de Valerian.
 *
 * Esta primera tanda no intenta completar cada profesión madre. Selecciona únicamente
 * especializaciones suficientes para que el jugador pueda reconstruir, a través de personas:
 * - la desaparición del viejo orden;
 * - la reconstrucción y el desorden posterior a la Marcha;
 * - el nacimiento de V881;
 * - la divergencia electroatmosférica/analógica;
 * - medicina frecuencial;
 * - locomoción terrestre, dominio marítimo y logística industrial.
 */
public final class SubprofessionProfileCatalog {
    private static final EnumMap<Subprofession,SubprofessionProfile> DATA=build();
    private SubprofessionProfileCatalog(){}

    public static SubprofessionProfile profile(Subprofession subprofession){
        SubprofessionProfile p=DATA.get(Objects.requireNonNull(subprofession));
        if(p==null) throw new IllegalArgumentException("Subprofesión sin perfil : "+subprofession);
        return p;
    }

    public static Map<Subprofession,SubprofessionProfile> all(){ return Map.copyOf(DATA); }

    private static EnumMap<Subprofession,SubprofessionProfile> build(){
        EnumMap<Subprofession,SubprofessionProfile> m=new EnumMap<>(Subprofession.class);

        putUnique(m,Subprofession.EBONY_WARRIOR_V881,2.00,"Kenan",
                """
                Kenan fue jornalero.

                Ese dato antecede a cualquier título que pudiera recibir después. No nació Guerrero de Ébano, no fue educado para pertenecer a una casta desaparecida y tampoco recibió de ella una posición que heredar. Cuando llegó a sus manos una armadura histórica de ébano, lo que quedaba de aquella figura era madera casi extinguida, piezas desgastadas y el recuerdo de hombres que después de la Primera Marcha Exaltada habían acabado muertos, desaparecidos, exiliados o mezclados con el resto de la población.

                La desaparición de aquellos hombres fue sólo la manifestación material de una pérdida mayor. El Guerrero de Ébano había pertenecido a una manera anterior de concentrar preparación, violencia, autoridad y prestigio. La Primera Marcha Exaltada dejó demasiado poco de aquel mundo para que vestir una armadura antigua bastara para recuperarlo.

                Kenan parlamentó con la OGC acerca de algo distinto a una restauración. La respuesta terminó haciendo posible que una institución extinguida pudiera volver a existir bajo unas condiciones que ya no eran las suyas. La madera heredada fue mineralizada; la protección fue reconstruida conforme al estándar V881; y una figura históricamente agotada obtuvo una segunda existencia compatible con una sociedad que había avanzado sin ella.

                Así apareció el primer Guerrero de Ébano de una nueva casta.

                Su posición se reconoce a la misma altura que la del Caballero V881, aunque ambos títulos hayan llegado hasta allí por caminos diferentes. El Caballero sobrevivió socialmente a su propio anacronismo y tuvo que ser reconstruido tecnológicamente. El Guerrero de Ébano tuvo que ser reconstruido también como institución.

                Que su primer representante contemporáneo proceda del jornal no es un detalle menor. Resume mejor que cualquier tratado lo que ocurrió después de la Primera Marcha Exaltada: el mundo que determinaba quién podía llegar a ser alguien ya no es exactamente el mismo.
                """);

        putSalary(m,Subprofession.RECONSTRUCTION_LABORER,.60,
                """
                Durante años hubo más cosas que reconstruir que manos cualificadas capaces de reconstruirlas.

                El peón no diseña una estación, no calcula una red de captación y no conoce necesariamente por qué una máquina responde cuando se energiza. Descarga, arrastra, excava, apuntala, demuele, limpia, eleva, clasifica y coloca aquello que alguien con un oficio especializado le indica. Una parte considerable de la reconstrucción material posterior a la Primera Marcha Exaltada pasó por personas cuya participación en el nuevo mundo consistió exactamente en eso.

                Trabaja junto a canteros, carpinteros, herreros, maestros y mercenarios contratados. Puede pasar una semana retirando restos de una construcción y la siguiente preparando la cimentación de una infraestructura cuyo funcionamiento apenas comprende. La expansión V881 no eliminó este trabajo. Lo multiplicó.

                Es una posición precaria. La fuerza física puede conseguirle trabajo hoy sin garantizarle trabajo mañana, y una lesión puede convertir rápidamente a un jornalero en alguien sin renta profesional. Pero también constituye una de las pocas puertas laborales que requieren más disposición que patrimonio, estudios o herramientas propias.

                Kenan conoció el mundo desde aquí antes de conocerlo desde ningún otro sitio.
                """);

        putSalary(m,Subprofession.ELECTROATMOSPHERIC_INFRASTRUCTURE_OPERATOR,.80,
                """
                No todo aquel que trabaja en una instalación electroatmosférica comprende la física que la gobierna.

                El operario instala soportes, mantiene despejadas superficies captadoras, inspecciona conexiones visibles, sustituye componentes normalizados, ayuda a tender conductores y ejecuta procedimientos de aislamiento bajo instrucciones que no está autorizado a reinterpretar. Los trabajos que exigen diagnóstico, cálculo o modificación del sistema corresponden a especialistas; los que exigen que kilómetros de infraestructura continúen físicamente en su sitio recaen en buena medida sobre él.

                Es una distinción importante. Una casa iluminada mediante captación electroatmosférica puede parecer una demostración de ciencia extraordinaria, pero detrás de ella siguen existiendo tornillos que se aflojan, superficies que se ensucian, aislantes que envejecen y estructuras que alguien debe inspeccionar.

                El estándar V881 no hizo desaparecer el mantenimiento. Consiguió que una civilización extraordinariamente avanzada siguiera necesitando personas con las manos sucias.
                """);

        putSalary(m,Subprofession.KINGDOM_AGENT,1.05,
                """
                La guerra terminó antes que el desorden que había producido.

                Después de la Primera Marcha Exaltada hubo regiones en las que propiedad, autoridad y violencia dejaron de coincidir durante demasiado tiempo. Aparecieron saqueadores, bandas, desertores, oportunistas, redes de contrabando y hombres armados que no reconocían otra jurisdicción que aquella que podían imponer. Recuperar una calle, una carretera o un asentamiento para la vida ordinaria exigió algo menos espectacular y más persistente que ganar otra batalla.

                De ahí procede buena parte del soldado que trata diariamente con civiles.

                Patrulla, interviene en delitos, custodia personas y bienes, protege instalaciones, controla alteraciones del orden y ejecuta las disposiciones de la autoridad competente. Cuando la situación lo exige, el mismo cuerpo puede movilizarse para funciones militares. La distinción contemporánea entre policía y soldado nunca llegó a cristalizar exactamente de la misma manera que en otras sociedades.

                El orden ha regresado lo suficiente para que un comerciante pueda abrir por la mañana esperando cerrar por la noche. No ha regresado lo suficiente para considerar terminado el trabajo.
                """);

        putSalary(m,Subprofession.V881_RIFLEMAN,1.20,
                """
                El fusilero posterior a la Primera Marcha Exaltada heredó el nombre de una profesión cuyo campo de batalla había cambiado por completo.

                No necesita una batería de artillería detrás para convertir una posición en un lugar inhabitable. Fusiles de repetición y bifilares, cañones antimaterial, armas de racimo y otras soluciones V881 trasladaron a unidades comparativamente pequeñas capacidades que antes habrían requerido sistemas logísticos mucho mayores. El resultado no fue un soldado invencible, sino uno al que se le exige comprender que el arma que porta puede alterar en segundos la geometría de un enfrentamiento.

                Su instrucción insiste tanto en el empleo como en la contención. Identificar cobertura, reconocer materiales, separar una amenaza del entorno civil y comprender las consecuencias de una descarga importan porque la diferencia entre capacidad individual y capacidad estratégica se ha estrechado peligrosamente.

                La infantería sigue existiendo porque ningún adelanto tecnológico ha conseguido que un territorio permanezca controlado sin alguien presente en él.
                """);

        putVariable(m,Subprofession.COMPANY_CONTRACTOR,1.50,
                """
                La Primera Marcha Exaltada produjo una cantidad de trabajo peligroso muy superior a la capacidad inmediata de las instituciones para absorberlo.

                Escoltar caravanas, recuperar instalaciones, proteger talleres, despejar rutas, asegurar obras, acompañar expediciones, reforzar asentamientos y combatir cuando el contrato lo exigía se convirtió en un mercado. A medida que V881 aceleró la reconstrucción, ese mercado dejó de contratar únicamente espadas.

                Un mercenario puede ser combatiente, pero también carpintero, cantero, mecánico o especialista en otra actividad que una compañía necesite vender allí donde no existe capacidad local suficiente. Lo que lo define no es su herramienta. Lo define la relación contractual mediante la que convierte una capacidad profesional transportable en servicio.

                Algunas compañías acumularon hombres, especialistas, equipamiento, capital y contratos suficientes para que sus gestores acabaran administrando territorios de facto, negociando con instituciones y compitiendo por recursos como auténticos señores de la guerra.

                Eso no convierte al mercenario ordinario en uno de ellos.

                Significa que pertenece al mismo mercado que hizo posible su aparición.
                """);

        putSalary(m,Subprofession.V881_ELECTROMECHANIC,1.50,
                """
                Hay máquinas V881 que todavía pueden desmontarse sobre una mesa.

                Ésa es precisamente una de sus virtudes.

                El electromecánico trabaja donde metalurgia, electricidad y movimiento dejan de ser disciplinas separadas: actuadores, bobinados, contactos, aislamiento, mecanismos de transmisión, conjuntos de precisión, sistemas de captación y componentes que deben continuar funcionando después de miles de ciclos mecánicos.

                Su oficio conserva mucho del herrero. Sigue importando cómo responde un metal a una carga, cómo se deforma una unión y cómo se repara una pieza. Lo que ha cambiado es que ahora un error de tolerancia puede alterar un circuito y un defecto eléctrico puede convertir la propia estructura metálica en parte del problema.

                V881 avanzó enormemente sin necesitar que cada objeto se convirtiera en una caja negra. Buena parte de sus máquinas siguen permitiendo que un especialista las abra, mida, comprenda y repare.

                Quizá por eso muchas han sobrevivido a personas que las utilizaron.
                """);

        putSalary(m,Subprofession.FREQUENCY_PHYSICIAN,1.45,
                """
                La medicina de Valerian aprendió a hacer una pregunta distinta.

                Una enfermedad puede describirse por lo que hace al cuerpo, por aquello que altera y por los signos que deja. El desarrollo científico posterior a la Primera Marcha añadió otra posibilidad: determinar una firma frecuencial suficientemente estable para reconocer determinadas alteraciones patológicas y actuar sobre ellas.

                El médico frecuencial no abandona anatomía, fisiología, exploración ni clínica. Las necesita para saber qué está observando. La frecuencia añade una herramienta que permite identificar y tratar fenómenos cuya manifestación material puede ser tardía, confusa o compartida con otras enfermedades.

                El descubrimiento transformó hospitales, investigación y veterinaria. El cuerpo de Maestro incluye médicos, cirujanos, veterinarios, investigadores y otros especialistas intelectuales: las subprofesiones distinguen qué clase de conocimiento ejercen.

                La misma capacidad de caracterizar una alteración mediante frecuencia tiene una contrapartida evidente. Aquello que puede medirse e interferirse puede ser estudiado desde ambas direcciones. Por eso determinadas líneas de investigación frecuencial están sometidas a controles que un ciudadano corriente nunca llega a conocer, y la guerra V881 no necesita reproducir exactamente los vectores biológicos de otras ramas históricas para convertir enfermedad y frecuencia en problemas estratégicos.
                """);

        putSalary(m,Subprofession.FREQUENCY_RESEARCHER,1.70,
                """
                Su laboratorio puede contener más osciladores que microscopios.

                El investigador frecuencial estudia la correspondencia entre materia viva, estados fisiológicos y respuestas reproducibles ante determinadas excitaciones. Clasifica patrones, establece márgenes, separa coincidencias de firmas consistentes y proporciona a médicos y veterinarios herramientas que puedan utilizar sin repetir toda la investigación que las produjo.

                Su disciplina explica una de las divergencias más profundas de V881. En lugar de convertir la descodificación exhaustiva del ADN en el eje dominante de la biomedicina, Valerian desarrolló una vía que ofrecía resultados operativos inmediatos: medir qué frecuencia caracteriza una alteración y qué intervención modifica su comportamiento.

                La ventaja es evidente en una sala clínica.

                La desventaja resulta igualmente evidente en manos de alguien cuya finalidad no sea curar.

                No todas las frecuencias catalogadas aparecen en manuales civiles.
                """);

        putSalary(m,Subprofession.ELECTROATMOSPHERIC_NETWORK_ENGINEER,1.80,
                """
                Diseña redes de captación electroatmosférica, calculando energía, ionización, geometría y materiales para que la infraestructura produzca potencia sin convertirse en una fuente de fallo.
                """);
        putSalary(m,Subprofession.ELECTROATMOSPHERIC_CAPTATION_ENGINEER,1.70,
                """
                Ajusta superficies captadoras, sensores, conexiones y tolerancias de campo. Su trabajo determina cuánto del fenómeno atmosférico llega realmente a la instalación.
                """);
        putSalary(m,Subprofession.ELECTROATMOSPHERIC_SAFETY_ENGINEER,1.90,
                """
                Supervisa rutas de descarga, aislamiento, zonas de exclusión y consecuencias territoriales de las redes electroatmosféricas. Decide cuándo una infraestructura debe detenerse.
                """);

        putSalary(m,Subprofession.ELECTROMAGNETIC_LOCOMOTION_SYSTEMS_ENGINEER,1.75,
                """
                Diseña locomotoras y sistemas de alimentación, guiado y frenado electromagnético. Su objeto profesional es la máquina y la estabilidad de sus secuencias.
                """);
        putSalary(m,Subprofession.RAILWAY_INFRASTRUCTURE_ENGINEER,1.90,
                """
                Diseña corredores, estaciones, alimentación, mantenimiento y continuidad operativa. Su objeto profesional es la red ferroviaria que debe sobrevivir a décadas de uso.
                """);
        putSalary(m,Subprofession.ELECTROMAGNETIC_TRANSPORT_PLANNER,1.85,
                """
                Planifica qué aparece alrededor de una línea estable: almacenes, talleres, población, suministro y autoridad. Trabaja sobre la relación entre transporte y territorio.
                """);

        putSalary(m,Subprofession.V881_NAVIGATOR,1.25,
                """
                El mar no fue abandonado cuando apareció V881. Fue reinterpretado.

                Un buque moderno dispone de varias superficies para obtener energía de aquello que ya lo rodea. Captadores aprovechan la energía del viento; las velas solares añaden otra vía de captación; y los motores reciben potencia procedente de la electricidad atmosférica para mantener una propulsión silenciosa y eficiente cuando la navegación exige algo que una vela convencional no podría proporcionar.

                Por eso llamar simplemente velero a uno de estos barcos describe su silueta mejor que su ingeniería.

                El navegante debe seguir comprendiendo viento, corriente, mar y meteorología, porque ninguna sofisticación convierte al océano en una superficie inerte. A ese conocimiento añade gestión energética y coordinación con sistemas cuya autonomía permite travesías extraordinariamente eficientes.

                La tecnología no sustituyó al marinero.

                Le dio más cosas que escuchar cuando mira el cielo.
                """);

        putSalary(m,Subprofession.NAVAL_RAILGUN_GUNNER,1.45,
                """
                En el mar todavía existen armas demasiado grandes para que un hombre pretenda llevarlas consigo.

                Los grandes cañones de riel V881 ocupan esa posición. Instalados en buques y defensas costeras, convierten energía eléctrica en aceleración electromagnética y permiten proyectar masas a distancias y velocidades que justifican por sí solas una especialidad profesional.

                El artillero no se limita a apuntar. Debe conocer alimentación, carga, estado del sistema, geometría de tiro y aquello que existe detrás del objetivo. Un disparo naval de esta clase no admite la despreocupación que podría tolerarse con un arma individual.

                Su existencia explica también por qué la revolución del armamento terrestre no hizo desaparecer el arma pesada.

                Simplemente la desplazó hacia aquellos lugares donde la escala sigue teniendo sentido.
                """);

        putSalary(m,Subprofession.NAVAL_ELECTROATMOSPHERIC_NETWORK_ENGINEER,1.40,
                """
                Bajo cubierta existe un barco distinto del que se ve desde el puerto.

                Allí la energía captada del viento, la captación solar y el campo eléctrico atmosférico dejan de ser fenómenos del paisaje y se convierten en potencia que debe recogerse, acondicionarse, distribuirse y finalmente transformar en trabajo mecánico. El maquinista vigila esa cadena.

                Su responsabilidad son motores, derivaciones, aislamiento, regulación y redundancias necesarias para que una avería no deje un buque convertido en masa inerte en mitad del océano. Los motores no queman combustible para imponerse al viento: aprovechan la energía atmosférica captada por el propio sistema naval.

                Las velas continúan siendo visibles porque siguen siendo útiles. La captación del viento existe porque el viento sigue siendo energía. Los motores existen porque el mar no siempre concede el régimen de navegación que necesita un capitán.

                V881 no eligió entre esos recursos.

                Aprendió a alimentarse de todos ellos.
                """);

        putVariable(m,Subprofession.V881_INDUSTRIAL_BROKER,1.70,
                """
                Una tecnología puede inventarse en un taller. Una civilización necesita que llegue a miles.

                El corredor industrial compra y vende acero, bronce, vidrio, caucho, componentes electromecánicos, instrumental y materiales especializados entre productores, talleres, compañías, instituciones y grandes obras. No necesita fabricar aquello que transporta; necesita saber quién puede hacerlo, quién lo necesita, cuánto tardará en llegar y cuánto riesgo existe entre ambos.

                El boom posterior a la Primera Marcha Exaltada convirtió esa información en capital.

                Un taller con capacidad extraordinaria pero sin materiales sigue siendo un edificio vacío. Una instalación terminada sin repuestos empieza a degradarse desde el primer día. Una compañía mercenaria equipada pero sin cadena de suministro dispone únicamente de aquello que pueda perder una vez.

                El corredor industrial comercia precisamente con esa diferencia entre poseer una tecnología y poder sostenerla.
                """);



        //  — normalización de la vida civil sobre la infraestructura V881.

        //  — descompresión del bloque artesanal.
        putSalary(m,Subprofession.STONE_SETTER,1.00,"""
                El cantero de obra recibe piezas y las convierte en estructura: presenta, mueve, aploma, asienta y corrige sillares, peldaños, dovelas y apoyos. Su especialidad es ejecutar bajo peso y polvo lo que otros han trazado, manteniendo geometría suficiente para que la obra continúe sin heredar errores acumulativos.
                """);
        putSalary(m,Subprofession.HIDE_PREPARER,0.82,"""
                El preparador de pieles trabaja antes del curtido propiamente dicho. Recibe materia todavía perecedera, la limpia, despoja de restos, recorta y acondiciona para que pueda entrar en proceso sin contaminar ni arruinar un lote. Es la fase más física y desagradable de una cadena que después puede parecer mucho más limpia de lo que empezó.
                """);
        putSalary(m,Subprofession.LEATHER_FINISHER_GRADER,1.02,"""
                La acabadora y clasificadora de cuero recibe material ya estabilizado y decide qué merece convertirse en correa, calzado, funda, protección o pieza de precisión. Examina espesor, flexibilidad, defectos, uniformidad y superficie; recorta, iguala y clasifica. Su trabajo transforma un lote curtido en material comercialmente legible.
                """);
        putSalary(m,Subprofession.PRECISION_PATTERNMAKER,1.12,"""
                La patronista de precisión convierte cuerpos y encargos en geometría reproducible. Toma o interpreta medidas, corrige proporciones, orienta el tejido, calcula márgenes y corta piezas cuya exactitud determina cuánto trabajo posterior será reparación de errores propios. No vende imagen de salón: produce la arquitectura de la prenda.
                """);
        putSalary(m,Subprofession.BENCH_CARPENTER,1.15,"""
                El carpintero de banco ocupa el territorio entre la estructura y la ebanistería. Fabrica puertas, marcos, cajas, componentes, uniones, reposiciones y mecanismos de madera que deben encajar, repararse y seguir funcionando. Su banco es una estación de medida y ajuste más que un escaparate.
                """);

        putSalary(m,Subprofession.STONEWORK_MASTER,1.20,
                """
                Una ciudad reconstruida deprisa puede mantenerse en pie. Una ciudad reconstruida bien puede sobrevivir a quienes la levantaron.

                El maestro de obra pétrea trabaja en esa diferencia.

                Selecciona piedra, reconoce estratos y defectos, decide despieces, calcula apoyos y dirige el asiento de sillares, arcos, escaleras, cimentaciones, muros de carga y estructuras cuya reparación resultaría más costosa que haberlas ejecutado correctamente desde el principio. Conoce la resistencia de aquello que talla, pero también la importancia de aquello sobre lo que descansa.

                El crecimiento posterior a la Primera Marcha Exaltada convirtió el oficio en algo más amplio que reconstruir ruinas. Estaciones, talleres, almacenes, edificios administrativos, viviendas y nuevas instalaciones necesitaron soportes duraderos para tecnologías cuya modernidad no vuelve menos obediente a la gravedad un edificio.

                Trabaja junto a carpinteros, herreros, jornaleros y maestros especializados. Puede dirigir a hombres que no saben calcular una carga y recibir instrucciones de otros que jamás han levantado una piedra.

                Entre ambos extremos queda su oficio.

                V881 puede cambiar aquello que una casa hace.

                No cambia la necesidad de que permanezca en pie.
                """);

        putSalary(m,Subprofession.PRECISION_STONECUTTER,1.10,
                """
                No toda piedra termina formando un muro.

                El tallista trabaja aquellas piezas en las que unos pocos milímetros separan el ajuste de la holgura: canalizaciones, apoyos, alojamientos, superficies de asiento, piezas arquitectónicas, conducciones protegidas y elementos que deben integrarse con metal, madera, vidrio o instalaciones técnicas.

                Sus herramientas siguen produciendo polvo.

                Lo que ha cambiado es la tolerancia que se espera de ellas.

                La expansión V881 multiplicó las construcciones en las que una estructura tradicional debía convivir con componentes extraordinariamente precisos. Eso no convirtió al cantero en ingeniero. Lo obligó a ser un cantero mejor.

                Un electromecánico puede entregar una máquina impecable. Un maestro puede haber calculado exactamente dónde debe trabajar. Si el alojamiento sobre el que ambos confían está mal ejecutado, la precisión termina allí.

                Hay tecnologías cuya última tolerancia todavía depende de una mano sosteniendo un cincel.
                """);

        putSalary(m,Subprofession.STRUCTURAL_CARPENTER,1.10,
                """
                La madera sobrevivió al acero, al vidrio técnico y a la electromecánica por una razón sencilla: continúa siendo extraordinariamente útil.

                El carpintero estructural construye entramados, cubiertas, forjados, escaleras, cerramientos, andamiajes, plataformas y estructuras auxiliares. Conoce qué piezas pueden flexar, cuáles deben permanecer inmóviles, cómo trabaja una unión y cuánto puede retirarse de una sección antes de que deje de cumplir su función.

                Durante la reconstrucción fue uno de los oficios que podían convertir materiales disponibles en refugio con mayor rapidez. Después del periodo de emergencia, la demanda no desapareció: cambió de calidad.

                Las viviendas volvieron a necesitar acabados. Los comercios, mostradores. Los talleres, bancos y estructuras auxiliares. Las estaciones y almacenes, elementos reemplazables que no justificaban recurrir a metal para cada función.

                V881 no construyó un mundo enteramente metálico.

                Construyó uno lo bastante sofisticado como para saber cuándo el metal era innecesario.
                """);

        putSalary(m,Subprofession.CABINETMAKER,1.25,
                """
                El ebanista trabaja cuando la utilidad ya no basta.

                Muebles, escritorios, estuches, instrumentos, interiores, cajas técnicas y encargos particulares pasan por manos capaces de conseguir que dos superficies ajusten antes incluso de que alguien piense en decorarlas.

                Después vienen la veta, el tacto, el equilibrio, el acabado y aquello que el cliente está dispuesto a pagar por distinguir un objeto necesario de un objeto suyo.

                El auge posterior a la reconstrucción produjo una clientela nueva. Comerciantes enriquecidos, maestros bien remunerados, gestores de compañías, profesionales liberales y familias que por primera vez podían destinar parte de su renta a algo distinto de sobrevivir comenzaron a encargar objetos que una generación anterior habría considerado secundarios.

                El oficio sirve por eso como un indicador económico involuntario.

                Cuando un hombre vuelve a discutir durante media hora sobre la madera de su escritorio, probablemente hace tiempo que dejó de preguntarse si mañana tendrá techo.
                """);

        putSalary(m,Subprofession.INDUSTRIAL_TANNER,0.95,
                """
                Antes de convertirse en una bota, una correa, una junta o una pieza de equipo, una piel es materia orgánica empeñada en descomponerse.

                El curtidor industrial trabaja contra ese proceso.

                Limpia, prepara, curte, engrasa, seca, selecciona espesores y clasifica piezas según aquello que deberán soportar. Una correa de transmisión, una protección, una funda y un zapato pueden compartir origen sin exigir el mismo cuero.

                La expansión industrial no redujo su mercado. Lo diversificó. Equipamiento militar, talleres, transporte, vestimenta, talabartería, cierres, protecciones y centenares de objetos cotidianos continúan necesitando materiales flexibles, reparables y resistentes.

                Es un oficio químicamente desagradable y físicamente exigente. La prosperidad puede mejorar instalaciones y procedimientos; no consigue que una piel llegue limpia al taller.

                Por eso el curtidor ocupa una posición curiosa en la sociedad reconstruida: casi todos necesitan alguna parte de su trabajo y muy pocos desean permanecer demasiado tiempo donde se realiza.
                """);

        putSalary(m,Subprofession.WORK_TAILOR,1.00,
                """
                Vestir a una población no consiste únicamente en cubrirla.

                El sastre de oficio toma medidas, adapta patrones, selecciona tejidos y construye prendas destinadas a soportar jornadas concretas. Un marinero, un médico, un comerciante y un jornalero no desgastan una prenda de la misma manera, ni necesitan bolsillos en los mismos lugares, ni trabajan bajo las mismas condiciones.

                La recuperación económica convirtió esa especialización en una necesidad cotidiana.

                Una sociedad que vuelve a tener profesiones estables vuelve también a producir uniformes, delantales, prendas de trabajo, ropa formal, ropa infantil, reparaciones y modificaciones. La estandarización puede proporcionar una talla aproximada. El sastre se ocupa de la persona que existe dentro.

                Su oficio también hace visible algo que las estadísticas económicas ocultan: dos individuos con la misma renta pueden pertenecer a mundos distintos por cómo deben presentarse ante los demás.

                La ropa vuelve legible una sociedad.

                El sastre aprende a leerla antes de cortarla.
                """);

        putVariable(m,Subprofession.SALON_DRESSMAKER,1.30,
                """
                Cuando la supervivencia deja de consumir toda la renta disponible, aparece el lujo de querer ser visto de una manera concreta.

                La modista de salón trabaja precisamente ahí.

                No vende únicamente tejido y costura. Interpreta silueta, ocasión, posición social, edad, profesión, reputación y la impresión que su cliente desea producir antes de haber pronunciado una palabra. Conoce qué puede insinuarse sin resultar impropio, qué resulta antiguo, qué acaba de ponerse de moda y qué sólo puede llevar alguien con suficiente seguridad para convertir una rareza en tendencia.

                El crecimiento de comerciantes, profesionales, compañías enriquecidas y nuevas fortunas creó un mercado que no necesitó esperar a que la vieja aristocracia lo autorizara.

                Por eso los salones de costura cuentan una parte especialmente silenciosa del cambio social posterior a la Marcha.

                El dinero nuevo aprendió pronto a vestirse.

                El dinero viejo tuvo que aprender a reconocerlo.
                """);

        putVariable(m,Subprofession.BARBER,0.80,
                """
                Pocas profesiones conocen tantas caras y necesitan saber tan poco de sus nombres.

                El barbero corta cabello, afeita, perfila barba, mantiene herramientas y atiende a trabajadores, soldados, comerciantes, viajeros y hombres que sólo pueden permitirse sentarse en su silla de vez en cuando. Su servicio es barato comparado con otros cuidados personales, repetitivo y suficientemente común para convertir el establecimiento en uno de los lugares donde distintos estratos sociales coinciden sin necesidad de compartir mesa.

                Allí circulan horarios, rumores, precios, contrataciones, accidentes, matrimonios y pequeñas desgracias mucho antes de que alguna de ellas merezca convertirse en documento.

                No es periodista.

                Tampoco necesita serlo.

                En una sociedad casi enteramente analógica, determinadas informaciones continúan viajando a la velocidad de una conversación.
                """);

        putVariable(m,Subprofession.SALON_HAIRDRESSER,1.05,
                """
                Hay una diferencia entre llevar el cabello limpio y llevarlo deliberadamente de una determinada manera.

                El peluquero de salón vive de ella.

                Trabaja forma, volumen, longitud, preparación, mantenimiento y presentación según el rostro, la ropa, la edad y el ambiente social de su cliente. Atiende particularmente a quienes convierten su presencia en parte de su actividad: comerciantes, cortesanas, artistas, profesionales de trato público y miembros de familias con suficiente renta para considerar la apariencia una inversión repetida.

                Su prosperidad resulta difícil de separar de la recuperación urbana.

                Durante el caos, presentarse bien era una preocupación secundaria.

                Cuando vuelve a importar cómo entra alguien en una habitación, significa que la habitación ha vuelto a ser suficientemente segura para preocuparse por ello.
                """);

        putVariable(m,Subprofession.ITINERANT_PUPPETEER_STORYTELLER,0.70,
                """
                Antes de llegar a una ciudad, puede que sus historias ya hayan llegado.

                El narrador ambulante viaja entre mercados, plazas, posadas y celebraciones llevando repertorios que mezclan acontecimientos conocidos, relatos tradicionales, sátira, exageración y ficción. Puede trabajar solo o acompañado de títeres, instrumentos, decorados ligeros y otros feriantes.

                Su oficio adquirió una importancia peculiar en un mundo donde la información no circula por una red digital universal.

                No sustituye al mensajero, porque nadie debería confiarle un documento urgente. Tampoco sustituye al maestro ni al registro oficial. Hace algo diferente: transporta imaginario.

                Una batalla puede convertirse en canción. Una figura pública, en caricatura. Una tecnología incomprensible para una aldea, en una historia sobre una máquina que bebe del cielo.

                Para cuando alguien corrige la exageración, quizá ya haya tres versiones mejores.
                """);

        putVariable(m,Subprofession.FAIRGROUND_ENTREPRENEUR,1.10,
                """
                Una feria parece espontánea sólo a quien llega cuando ya está montada.

                Alguien ha negociado el terreno, reunido artistas, contratado transporte, distribuido puestos, calculado comida, iluminación y seguridad, pagado permisos cuando corresponden y decidido qué atracción merece viajar hasta el siguiente asentamiento.

                Ese alguien es el empresario de feria.

                La recuperación de rutas y mercados después de la Primera Marcha hizo posible que el entretenimiento volviera a moverse con cierta regularidad. Donde el orden sigue siendo débil puede contratar mercenarios. Donde la administración es fuerte necesitará permisos. Donde existe dinero suficiente atraerá comerciantes además de espectadores.

                Su renta puede superar ampliamente la de un feriante ordinario o desaparecer después de una mala temporada.

                La diversión también tiene logística.
                """);

        putSalary(m,Subprofession.KINGDOM_MESSENGER,1.15,
                """
                Hay información que debe llegar exactamente como salió.

                El Mensajero del Reino existe para esa clase de información.

                Transporta documentos, órdenes, credenciales, correspondencia institucional y otros contenidos cuya autenticidad, custodia o entrega justifican confiar en una persona identificable antes que en una cadena informal de voces. Debe conocer rutas, protocolos de recepción, prioridades y procedimientos para demostrar que un envío no ha sido sustituido durante el trayecto.

                Su profesión puede parecer anacrónica junto a la ingeniería V881 hasta que se comprende el mundo en el que trabaja.

                Valerian dispone de comunicaciones especializadas, pero no construyó una red digital universal de la que dependa cada ciudadano, empresa e institución. El papel continúa siendo extremadamente difícil de confundir con una transmisión inexistente, y una persona puede ser interrogada acerca de aquello que le ocurrió entre origen y destino.

                La tecnología hizo algunos mensajes instantáneos.

                No hizo innecesaria la custodia.
                """);

        putSalary(m,Subprofession.SURGEON,1.60,
                """
                La frecuencia puede reconocer una enfermedad.

                No puede volver a colocar por sí sola un hueso donde estaba.

                El cirujano trabaja en el límite material de la medicina V881: heridas, hemorragias, fracturas, cuerpos extraños, tejido destruido y todas aquellas situaciones en las que comprender perfectamente qué ocurre no elimina la necesidad de intervenir físicamente.

                Su formación combina anatomía, fisiología, asepsia, control del dolor, reparación tisular y criterio para distinguir aquello que debe tocarse de aquello que sobrevivirá mejor si no se toca.

                La medicina frecuencial amplió enormemente lo que puede diagnosticarse y tratarse. No abolió la anatomía.

                Las armas V881 se encargaron de que nadie pudiera olvidarlo.
                """);

        putSalary(m,Subprofession.VETERINARIAN,1.25,
                """
                Una sociedad que todavía depende de animales no puede permitirse que su medicina termine en la piel humana.

                El veterinario atiende ganado, animales de trabajo, monturas y otras especies cuya salud puede representar alimento, transporte, patrimonio o simplemente compañía. Conoce anatomías distintas, enfermedades compartidas y patologías que nunca aparecerán en un paciente humano.

                La ciencia frecuencial le proporciona el mismo principio general utilizado por otros especialistas médicos: caracterizar alteraciones mediante respuestas y firmas reproducibles. Su aplicación exige, sin embargo, conocer qué significa normalidad en cada especie antes de declarar que una frecuencia representa enfermedad.

                También vigila aquello que puede pasar de animal a población y aquello que sólo parece hacerlo.

                En un mundo que ha aprendido a medir enfermedades de maneras extraordinariamente precisas, el veterinario sigue comenzando muchas consultas con una pregunta antiquísima:

                «¿Desde cuándo hace eso?»
                """);

        putSalary(m,Subprofession.PUBLIC_SCRIBE,1.25,
                """
                Una sociedad analógica puede ser extraordinariamente sofisticada siempre que recuerde quién prometió qué.

                El escribano convierte acuerdos, compraventas, poderes, declaraciones, inventarios, deudas, transmisiones patrimoniales y otros actos privados en documentos cuya existencia puede demostrarse después de que las partes hayan empezado a recordar cosas diferentes.

                Comprueba identidades, conserva fórmulas, registra fechas y sabe qué debe contener un escrito para que otra autoridad pueda entenderlo años más tarde.

                La expansión económica posterior a la Marcha multiplicó su trabajo. Nuevos talleres, compañías, contratos, propiedades reconstruidas y capitales emergentes produjeron algo casi tan rápido como produjeron riqueza:

                disputas sobre a quién pertenecía.

                Una firma no impide mentir.

                Hace más difícil fingir que nunca se dijo nada.
                """);

        putSalary(m,Subprofession.MAGISTRATE,1.80,
                """
                Restaurar el orden no consiste únicamente en poner soldados en una calle.

                En algún momento alguien tiene que decidir qué hacer cuando dos personas reclaman la misma propiedad, cuando un contrato se incumple, cuando una detención llega ante la autoridad o cuando el hombre acusado insiste en que la fuerza que lo arrestó fue precisamente quien infringió la norma.

                El magistrado trabaja allí.

                Examina actuaciones, testimonios, documentos y normas aplicables; determina responsabilidades y convierte una disputa concreta en una resolución que otras instituciones puedan ejecutar.

                Su importancia aumentó conforme el Reino recuperaba espacios donde durante años la violencia había sustituido al procedimiento. Cada sentencia obedecida sin necesidad de reunir una partida armada representa una pequeña victoria institucional.

                El problema es que una resolución sólo vale tanto como la capacidad del orden que la respalda.

                Y Valerian todavía está averiguando hasta dónde llega la suya.
                """);

        putVariable(m,Subprofession.SHOPKEEPER,1.10,
                """
                El gran comercio mueve materiales entre regiones.

                El tendero consigue que alguien pueda comprar jabón el martes.

                Compra cantidades que una familia no necesita, las divide en cantidades que sí necesita, mantiene existencias, conoce proveedores, asume pérdidas, adelanta dinero y aprende qué productos desaparecen del estante antes incluso de saber por qué ha cambiado la demanda.

                Su establecimiento es la última parte visible de cadenas mucho mayores.

                Detrás de una herramienta puede haber un herrero, un transportista y un corredor industrial. Detrás de una prenda, modistas, curtidores o productores de tejidos. Delante sólo queda una persona preguntando cuánto cuesta.

                Después de la Primera Marcha, la reapertura estable de comercios fue una señal humilde pero decisiva de recuperación.

                Una calle puede estar oficialmente pacificada.

                Cuando alguien vuelve a dejar mercancía expuesta junto a la puerta, empieza a parecerlo.
                """);

        putSalary(m,Subprofession.RAILWAY_GUARD,1.15,
                """
                Una vía férrea conecta ciudades.

                También conecta cualquier problema existente entre ellas.

                El guardia ferroviario protege estaciones, convoyes, pasajeros, carga e infraestructura crítica. Investiga robos, interviene en altercados, vigila puntos vulnerables y coordina su actuación con autoridades locales cuando un incidente empieza en una jurisdicción y termina muchas leguas después.

                Las locomotoras electromagnéticas hicieron posible transportar enormes cantidades de mercancía con regularidad.

                Eso convirtió cada convoy en una concentración periódica de valor.

                Durante los años más caóticos, sabotaje, robo y asalto podían interrumpir algo más importante que un viaje: podían dejar talleres sin materias primas o asentamientos sin suministros.

                La tecnología resolvió el movimiento del tren.

                La seguridad tuvo que resolver todo lo demás.
                """);



        //  — vida doméstica, movilidad personal, ocio, marginalidad y Cortesana.


        putNone(m,Subprofession.PRISONER,
                """
                El preso permanece dentro de la profesión Mendigo mientras no ejerza una actividad remunerada reconocida.

                Puede haber sido jornalero, soldado, comerciante o jurista el día anterior. La condena no borra necesariamente aquello que sabe hacer; suspende la posición desde la que podía hacerlo libremente.

                Las prisiones contemporáneas disponen, en las regiones bien mantenidas, de saneamiento, iluminación y atención médica que una generación anterior habría considerado lujos. Eso no vuelve libre a quien vive detrás de una puerta cerrada.

                La sociedad analógica tiene además una relación particular con el expediente. Lo que consta en papel, quién lo firmó, qué autoridad ordenó el traslado y qué persona conserva la custodia importan enormemente porque no existe una red universal capaz de reconstruir automáticamente cada decisión administrativa.

                En zonas donde el orden institucional es débil, esa dependencia de personas y documentos puede proteger contra un poder central omnisciente.

                También puede dejar a alguien a merced de quienes controlen físicamente el archivo, la llave y el camino.
                """);

        putNone(m,Subprofession.UNEMPLOYED,
                """
                El desempleado demuestra por qué Mendigo no significa indigente.

                Puede conservar vivienda, ahorros, familia, herramientas y una educación completa. Lo único que no tiene en ese momento es una actividad profesional que produzca renta.

                En una ciudad V881 desarrollada esa caída puede resultar sorprendentemente soportable durante un tiempo. La luz doméstica no exige una factura energética semejante a la de otras ramas industriales; el agua caliente y el alcantarillado forman parte de una infraestructura madura; la medicina evita que una enfermedad ordinaria convierta automáticamente un mal mes en una catástrofe.

                El problema aparece cuando el tiempo se alarga.

                Sin renta no hay reposición ilimitada de ropa, alimentos, transporte, ocio ni patrimonio. Y en una sociedad donde la reconstrucción ha convertido la competencia técnica en una forma muy visible de valor personal, quedarse fuera del trabajo puede erosionar algo más que la bolsa.

                A veces una ciudad necesita trabajadores y una persona no encuentra su lugar en ella.

                Ambas cosas pueden ser ciertas al mismo tiempo.
                """);

        putNone(m,Subprofession.WORK_DISABLED,
                """
                La medicina V881 puede reparar una cantidad extraordinaria de daño.

                No todo daño devuelve automáticamente a una persona al oficio que tenía.

                El incapacitado laboral es quien no puede ejercer de manera estable una actividad que produzca renta suficiente, ya sea por lesión, enfermedad, deterioro funcional, secuela o una combinación que haga inseguro exigirle el rendimiento ordinario de su profesión. Puede conservar autonomía, conocimientos, familia y una vida larga. La incapacidad profesional no equivale a incapacidad humana.

                La paradoja se vuelve especialmente visible en los centros avanzados. Un cuerpo puede recibir tratamiento de enorme sofisticación y seguir sin soportar doce horas de carga, precisión manual, exposición al mar o servicio armado.

                La civilización ha conseguido separar con bastante eficacia enfermedad de muerte.

                Todavía no ha conseguido separar por completo capacidad productiva de posición social.

                Por eso algunos de sus ciudadanos pueden vivir muchos años después de haber dejado de saber dónde colocar esos años.
                """);

        putNone(m,Subprofession.INDIGENT,
                """
                El indigente es la forma de Mendigo que nadie necesita explicar dos veces.

                Carece de una renta estable y de recursos suficientes para sostener por sí mismo vivienda, alimentación y protección material. Puede dormir bajo techo prestado, depender de instituciones, favores o pequeñas economías informales y moverse entre lugares donde la infraestructura pública es excelente sin poseer casi nada dentro de ella.

                En una ciudad plenamente integrada en V881, pobreza no significa necesariamente beber aguas fecales, vivir a oscuras o carecer de cualquier tratamiento médico. Esa diferencia importa.

                También hace la indigencia más extraña.

                Puede existir agua caliente detrás de una pared a la que no tiene derecho de acceso. Puede pasar una locomotora silenciosa cargada de mercancías mientras él calcula cuánto puede gastar en comida. Puede observar una tecnología capaz de mantener con vida cuerpos durante décadas y seguir sin disponer de un lugar donde guardar una camisa seca.

                La abundancia técnica no elimina automáticamente la exclusión.

                Sólo cambia aquello que resulta imperdonable no haber resuelto.
                """);

        putNone(m,Subprofession.DISPLACED_RESIDENT,
                """
                Hay lugares que no fueron destruidos.

                Simplemente dejaron de quedar en medio de algo importante.

                Cuando un yacimiento se agota, una ruta cambia, una estación nunca llega o una región pierde interés político y estratégico, la diferencia tecnológica entre dos zonas del mismo Reino puede crecer durante años sin que exista una frontera entre ellas. El habitante desplazado procede de esos espacios o acaba abandonándolos cuando permanecer allí deja de ofrecer una vida sostenible.

                No huye necesariamente de una guerra.

                Puede huir de la ausencia de motivos para que nadie construya nada.

                La red ferroviaria no pasa por todos los pueblos. Los especialistas no se distribuyen uniformemente. Una instalación que en la capital se repara esa tarde puede permanecer averiada durante meses en una comarca desconectada. Caballos y bicicletas recuperan allí una importancia que ningún plano de una gran ciudad permite comprender.

                Cuando esas personas llegan a un núcleo desarrollado, descubren que no han viajado únicamente en el espacio.

                Han avanzado años.

                La desigualdad territorial de V881 no procede de desconocer cómo mejorar esos lugares.

                Procede de que saber hacerlo y decidir hacerlo son cosas diferentes.
                """);

        putVariable(m,Subprofession.SEX_WORKER,1.05,
                """
                El sexo puede convertirse en trabajo sin que todas las personas que lo venden vivan la misma historia.

                La trabajadora sexual cobra por encuentros cuya dimensión principal es erótica. Puede ejercer de manera independiente, dentro de una casa estable, de forma ocasional o en condiciones donde la distancia entre elección y necesidad económica resulte difícil de medir desde fuera.

                La recuperación urbana creó clientela, habitaciones seguras, iluminación nocturna y una vida social suficientemente estable para que el mercado sexual volviera a ser visible. El mismo crecimiento produjo su reverso: intermediarios abusivos, deuda, drogas, contrabando y espacios donde una actividad voluntaria puede degradarse hasta convertirse en explotación.

                La profesión Cortesana no presume que eso ocurra.

                Tampoco finge que no ocurre.

                En un mundo sin microvigilancia digital permanente, una persona puede conservar una intimidad que resultaría difícil en otras sociedades. Esa privacidad protege a muchos.

                También protege a quienes prefieren que determinadas habitaciones permanezcan fuera de la mirada pública.
                """);

        putVariable(m,Subprofession.SALON_COURTESAN,1.70,
                """
                La cortesana de salón vende algo que no cabe bien en la palabra prostitución.

                Puede existir sexo o no existirlo. Lo constante es la capacidad de acompañar a personas cuya vida pública exige conversación, presencia, discreción, educación, apariencia y comprensión suficiente de varios mundos para no convertirse en una invitada accidental dentro de ninguno.

                Escucha a comerciantes que hablan de rutas, mercenarios que hablan de contratos, juristas que hablan de conflictos y maestros que necesitan durante unas horas no explicar aquello en lo que trabajan. Aprende nombres, silencios, preferencias y límites.

                Su renta puede superar con facilidad la de muchas profesiones cualificadas porque una clientela acomodada no paga únicamente tiempo.

                Paga la ausencia de fricción social.

                Eso le concede una posición extraña: puede no poseer autoridad formal y, sin embargo, comprender antes que muchos funcionarios qué personas han empezado a encontrarse, quién ha dejado de hacerlo y qué fortuna nueva intenta aprender las costumbres de la antigua.

                Una sociedad presencial produce intermediarios humanos allí donde una red produciría perfiles.

                Ella es uno de los más sofisticados.
                """);

        putVariable(m,Subprofession.PROFESSIONAL_COMPANION,1.35,
                """
                Hay personas que pagan por no estar solas y no esperan que el encuentro termine en una cama.

                La acompañante profesional conversa, pasea, asiste a una comida, acompaña a una feria, ayuda a sostener una presencia pública o comparte unas horas con alguien que desea compañía sin convertir esa relación en amistad, romance ni obligación futura.

                La profesión creció con las ciudades recuperadas y con una población cuya esperanza de vida permite acumular años, pérdidas, divorcios, viudedades, desplazamientos y periodos enteros en los que la red social de una persona ya no coincide con el lugar donde vive.

                Su trabajo parece frívolo hasta que se recuerda cuánto tiempo puede vivir alguien en Valerian.

                La longevidad no garantiza compañía.

                La alta calidad material tampoco.

                En una sociedad donde casi todo encuentro significativo sigue ocurriendo entre cuerpos presentes en algún sitio, pagar por presencia puede resultar menos extraño que pagar por una máquina que finja ofrecerla.
                """);

        putSalary(m,Subprofession.STABLE_HAND,0.70,
                """
                La existencia de locomotoras electromagnéticas no consiguió que el caballo dejara de comer.

                El mozo de cuadras alimenta, cepilla, limpia, inspecciona cascos, prepara monturas, mueve animales, mantiene establos y aprende a reconocer qué caballo está simplemente cansado y cuál necesita un veterinario. Trabaja con Caballos de Paseo, de Carreras y de Tiro, tres soluciones distintas para necesidades que ninguna infraestructura resuelve por sí sola.

                El Caballo de Paseo ofrece equilibrio para jornadas y copiloto. El Caballo de Carreras sacrifica margen de carga por velocidad. El Caballo de Tiro convierte masa y fuerza sostenida en transporte donde el camino importa más que la prisa.

                En las zonas tecnológicamente desconectadas los tres pueden ser más útiles que una máquina extraordinaria para la que no existe taller próximo.

                Incluso en las ciudades continúan teniendo sentido en trayectos, trabajos y personas que prefieren no depender de una línea fija.

                V881 no extinguió la tracción animal.

                La volvió una elección mucho más consciente.
                """);

        putSalary(m,Subprofession.CYCLIST_MESSENGER,1.00,
                """
                El mensajero ciclista trabaja en la distancia demasiado larga para recorrerla a pie y demasiado corta para justificar una infraestructura mayor.

                La Bicicleta Plegable V881 le permite cargar el vehículo cuando el terreno deja de admitir ruedas o cuando debe subir a otro transporte sin abandonar su movilidad al llegar. La Bicicleta Militar V881 sacrifica esa portabilidad a cambio de robustez, portaequipajes y bolsas capaces de sostener documentos y equipo durante jornadas completas.

                No necesita combustible ni una red energética propia.

                Necesita camino, piernas y tiempo.

                En núcleos densos puede cruzar la ciudad evitando dependencias mayores. En zonas rurales puede enlazar una estación con pueblos a los que la vía nunca llegará. Y cuando una comunicación debe seguir siendo física por razones de autenticidad o custodia, la bicicleta convierte un cuerpo humano en el último segmento de una red que deliberadamente nunca se volvió enteramente digital.

                Su tecnología es sencilla.

                Su utilidad no.
                """);

        putVariable(m,Subprofession.MOTORCYCLE_COURIER,1.35,
                """
                La Motocicleta Cardán V881 ocupa el espacio entre la bicicleta y la infraestructura estratégica.

                Es pesada, robusta, capaz de llevar copiloto y maletas laterales y suficientemente rápida para convertir caminos largos en encargos de una jornada. El correo motociclista la utiliza cuando el tiempo importa, la ruta cambia demasiado para depender del ferrocarril y la carga todavía cabe en un vehículo individual.

                Puede transportar documentación, piezas, muestras, herramientas o encargos que una compañía necesita mover sin organizar un convoy.

                Su independencia tiene un precio.

                La motocicleta requiere mantenimiento, conocimientos mecánicos y una ruta que permita aprovecharla. En regiones abandonadas tecnológicamente puede convertirse en la máquina más avanzada de varios kilómetros a la redonda; si se avería allí, ese prestigio dura exactamente hasta que haga falta una pieza que nadie sabe fabricar.

                Por eso el correo motociclista aprende pronto la diferencia entre velocidad y alcance.

                La primera pertenece a la máquina.

                El segundo pertenece a la red humana que puede mantenerla.
                """);

        putVariable(m,Subprofession.ROAD_GUIDE,1.10,
                """
                El guía de caminos conoce aquello que desaparece cuando un mapa se imprime.

                Sabe qué puente continúa en pie, qué senda admite un caballo de tiro, dónde una bicicleta puede pasar después de la lluvia, qué localidad conserva un herrero, dónde conviene llenar un odre y qué tramo de carretera ha empezado a atraer más ladrones que comerciantes.

                Su oficio gana valor en los bordes de la infraestructura.

                El ferrocarril hace extraordinariamente predecible el viaje entre puntos conectados. El guía trabaja precisamente en todo lo que ocurre después de bajar del tren.

                Puede acompañar comerciantes, médicos, cazadores, mercenarios, mensajeros o familias desplazadas. No necesita poseer el transporte de sus clientes; necesita entender qué puede hacer cada uno.

                Por eso reconoce la diferencia práctica entre Caballo de Paseo, Caballo de Carreras y Caballo de Tiro; entre Bicicleta Plegable V881 y Bicicleta Militar V881; y entre una Motocicleta Cardán V881 que puede atravesar una comarca en horas y la misma motocicleta inmóvil porque la siguiente pieza de repuesto está a tres días.

                El último kilómetro siempre pertenece a alguien.
                """);

        putVariable(m,Subprofession.WILDLIFE_TRACKER,0.95,
                """
                El rastreador de fauna convierte señales pequeñas en presencia concreta. Huellas, pelo, excrementos, ramas, barro, dormideros, restos de alimentación y cambios de comportamiento le permiten reconstruir por dónde pasó un animal, cuándo y con qué probabilidad volverá.

                No necesita matar para que su trabajo tenga valor. Puede conducir a un cazador, localizar una especie para un naturalista, advertir a una aldea de un depredador o determinar que una zona aparentemente vacía sigue ocupada.

                Su especialidad se separa del guía de caminos porque lee tránsito animal, no infraestructura humana; del cazador profesional porque la localización es el producto principal, no la extracción; y del trampero porque no necesita preparar una captura para demostrar que entendió el patrón.

                En los márgenes del mundo V881, saber que algo estuvo allí hace seis horas puede valer más que verlo demasiado tarde.
                """);

        putSalary(m,Subprofession.DOMESTIC_V881_INSTALLER,1.25,
                """
                Una casa V881 plenamente integrada puede iluminarse, disponer de agua caliente y mantener servicios domésticos esenciales sin vivir pendiente de una factura energética periódica.

                Eso no significa que se construya sola.

                El instalador doméstico dispone captadores, conducciones, aislamiento, derivaciones, reguladores, iluminación, calentamiento y puntos de servicio de manera que una vivienda pueda aprovechar la infraestructura electroatmosférica sin convertir cada tormenta en una amenaza interior.

                Trabaja en la frontera entre el taller y la intimidad doméstica.

                Una instalación bien hecha debería resultar aburrida para quien vive con ella. La luz aparece cuando se necesita. El agua se calienta. Una protección actúa sin pedir atención. Sólo cuando algo está mal diseñado el habitante descubre cuánta ingeniería había detrás de no pensar en ello.

Descargas estáticas, incendios localizados y tormentas eléctricas anómalas forman parte de las contingencias que un instalador aprende a reconocer sin necesidad de conocer quién, qué o por qué produjo la perturbación que alcanzó la red.

                El desarrollo desigual del Reino se vuelve particularmente visible en su trabajo.

                En una ciudad puede renovar una vivienda en una mañana.

                En una zona desconectada puede descubrir que antes de mejorar una casa habría que mejorar todo aquello a lo que esa casa debería conectarse.
                """);

        putSalary(m,Subprofession.SANITATION_OPERATOR,0.75,
                """
                El alcantarillado sano es una de las tecnologías que dejan de parecer tecnología en cuanto funcionan.

                El operario de saneamiento inspecciona conducciones, retira obstrucciones, mantiene pendientes, registros, separación de redes y estaciones donde las aguas deben acondicionarse antes de volver al entorno o a circuitos permitidos. Trabaja bajo calles que otros ciudadanos utilizan precisamente para no pensar en lo que pasa debajo.

                Valerian aprendió a sanear agua mediante tratamientos vibracionales controlados además de filtración y manejo físico. El operario no determina esos parámetros: mantiene la infraestructura que permite aplicarlos de forma estable.

                La diferencia importa.

                Una frecuencia correcta en un laboratorio no arregla una tubería rota.

                Cuando el sistema funciona, enfermedades que antiguamente acompañaban a cualquier ciudad densa dejan de formar parte inevitable de la vida urbana.

                Cuando una red es abandonada, saboteada o alterada, esa seguridad puede deteriorarse con una rapidez que recuerda por qué el saneamiento nunca fue un lujo.
                """);

        putSalary(m,Subprofession.SANITARY_MASTER,1.45,
                """
                El Maestro sanitario trabaja para que agua limpia deje de ser una esperanza y se convierta en una condición verificable.

                Supervisa calidad, tratamiento, redes de distribución, respuesta vibracional, contaminación, mantenimiento y los límites dentro de los cuales una instalación puede considerarse segura. Coordina operarios y decide cuándo una anomalía exige cerrar un tramo antes de que la población descubra el problema por sus propios cuerpos.

                El progreso V881 hizo posible acondicionar agua mediante vibraciones controladas además de procedimientos físicos ordinarios. Esa capacidad elevó enormemente la salud urbana.

                También demostró algo incómodo: un sistema capaz de modificar de manera beneficiosa determinadas condiciones puede producir daño cuando aquello que llega a la población deja de coincidir con el régimen esperado.

                El Maestro sanitario civil estudia esa posibilidad para impedirla y detectarla, no para convertirla en una receta.

                En una sociedad donde el agua puede cuidarse con enorme precisión, una alteración sanitaria localizada rara vez se interpreta ya como simple mala suerte sin antes revisar la red.
                """);

        putVariable(m,Subprofession.TAVERN_KEEPER,1.15,
                """
                Una taberna vende bebida, comida y un lugar en el que permanecer sin necesidad de justificar demasiado por qué uno está allí.

                En una sociedad presencial eso la convierte en infraestructura social.

                El tabernero conoce proveedores, clientes habituales, viajeros, horarios de tren, compañías de paso y los ritmos de un barrio. Mantiene cocina, almacenaje, mesas, habitaciones cuando las hay y una tolerancia calculada hacia conversaciones que no son asunto suyo mientras no se conviertan en una pelea que sí lo sea.

                La luz barata y el agua caliente hacen posible mantener abierto un local con una comodidad que otras épocas habrían reservado a establecimientos ricos. No eliminan el coste de alimentos, salarios, mantenimiento ni alquiler.

                Tampoco eliminan el mercado clandestino que puede crecer alrededor de una sala llena de gente.

                Una taberna puede ser simplemente una taberna.

                Precisamente por eso sirve tan bien para que se encuentren personas que no quieren parecer reunidas.
                """);

        putVariable(m,Subprofession.BOOKSELLER,1.20,
                """
                El librero vive en una civilización que nunca tuvo que fingir que el papel era un residuo del pasado.

                Compra, conserva, clasifica y vende manuales, tratados, novelas, mapas, partituras, publicaciones técnicas, folletos, reediciones y documentos destinados a circular sin convertirse en expedientes oficiales. Conoce qué títulos buscan los estudiantes, qué edición corrige un error conocido y qué libro desaparece de los estantes cada vez que ocurre algo que hace relevante una idea antigua.

                La ausencia de una red digital universal concede al libro una persistencia particular.

                No necesita electricidad para seguir diciendo lo mismo mañana.

                Eso lo hace lento de actualizar y difícil de borrar a distancia.

                Las librerías son por ello lugares de ocio, estudio, memoria y circulación intelectual. Algunas sobreviven vendiendo manuales prácticos. Otras viven de lectores que ya tienen resueltas necesidades mucho más urgentes.

                Una sociedad que vuelve a leer por placer ha recuperado algo.

                Una sociedad que conserva libros que preferiría olvidar también.
                """);

        putVariable(m,Subprofession.COMPETITION_RIDER,1.05,
                """
                El jinete de competición convierte la monta en una disciplina de rendimiento medible. No trabaja transportando personas ni cuidando cuadras: entrena salidas, ritmo, trazada, equilibrio y lectura de la montura para carreras donde una mala decisión castiga tanto al caballo como al jinete.

                Su oficio existe porque V881 conserva circuitos ecuestres, apuestas, premios y criadores que comparan animales mediante resultados públicos. El Caballo de Carreras es parte de su patrimonio profesional cuando es propio, no un símbolo automático de riqueza.
                """);
        putVariable(m,Subprofession.V881_MOTORCYCLE_RACER,1.15,
                """
                La piloto de motociclismo V881 compite con la máquina disponible en su época: una motocicleta de cardán robusta cuya masa obliga a construir velocidad desde frenada, línea, transferencia de peso y conservación mecánica. Las pruebas valoran regularidad y control además de la punta de velocidad.

                El trabajo no consiste en conducir deprisa por carretera. Entrena, reconoce averías, prepara la máquina y acepta que una caída puede terminar una temporada.
                """);
        putVariable(m,Subprofession.COMPETITION_CYCLIST,0.95,
                """
                La ciclista de competición utiliza una bicicleta plegable V881 como máquina deportiva ligera: cadencia, trazada, ahorro de energía y reparación de campaña determinan una disciplina en la que el propio cuerpo sigue siendo el motor.

                Compite porque existen carreras y premios, pero su jornada ordinaria se parece más a entrenar, ajustar y recuperar que a la imagen de cruzar una meta.
                """);
        putVariable(m,Subprofession.TRIATHLETE,1.00,
                """
                El triatleta compite encadenando desplazamiento terrestre, ciclismo, natación y tramos de trepa o desnivel cuando el recorrido lo exige. Su valor no procede de dominar una sola acción al máximo, sino de conservar técnica mientras cambia de medio y acumula fatiga.

                V881 ya posee esas formas de locomoción; la subprofesión simplemente convierte su combinación en una actividad competitiva reconocible.
                """);
        putVariable(m,Subprofession.TAVERN_MUSICIAN,0.75,
                """
                El músico de taberna convierte una habitación común en un lugar al que alguien decide regresar.

                Toca para conversaciones, bailes, celebraciones, despedidas y noches en las que el público apenas presta atención hasta que deja de sonar. Puede viajar con un repertorio propio, aprender canciones locales o descubrir que una melodía nacida en una ciudad ha llegado a otra antes que él.

                En Valerian el ocio continúa teniendo cuerpos.

                La música no llega principalmente desde una plataforma invisible que conoce los gustos de cada individuo. Alguien tiene que tocarla, copiarla, enseñarla o llevarla consigo.

                Eso vuelve la experiencia menos eficiente y mucho más difícil de aislar de las personas que la producen.

                Una canción puede convertirse en memoria común sin que nadie haya registrado quién la escuchó.

                Para una sociedad que carece de microcontrol permanente, esa clase de anonimato compartido resulta tan cotidiana que pocos piensan en llamarla libertad.
                """);

        putVariable(m,Subprofession.GAME_MASTER,0.90,
                """
                Cartas, dados, tableros, acertijos, competiciones y apuestas permiten convertir una tarde en algo que parece importar muchísimo durante unas horas.

                El Maestro de juegos organiza ese pequeño mundo.

                Explica reglas, arbitra disputas, conserva material, prepara torneos y sabe cuándo una apuesta forma parte del entretenimiento y cuándo la mesa ha empezado a atraer deudas, fraude o personas que viven de que otros pierdan.

                El ocio analógico tiene una propiedad incómoda para cualquier intento de observarlo desde lejos: la mayor parte de lo que ocurre desaparece cuando los participantes se levantan.

                No existe un historial universal de cada partida.

                Quedan testigos, fichas, dinero cambiado de manos y reputaciones.

                Esa ausencia de registro protege el juego inocente y facilita también mercados clandestinos, apuestas amañadas y circulación de sustancias o mercancías que encuentran en los espacios de ocio un lugar natural para mezclarse con clientes.

                La misma privacidad sirve para cosas muy distintas.

                V881 no resolvió ese dualismo.

                Lo hizo más humano.
                """);



        //  — producción primaria, recursos, mercancías y economía periférica.
        putSalary(m,Subprofession.FARMER,0.80,
                """
                La tierra no sabe si pertenece al estándar V881.

                El agricultor sí.

                Cultiva cereal, legumbres, tubérculos y otras producciones extensivas destinadas a alimentar poblaciones que hace décadas dejaron de depender de aquello que pudiera crecer junto a sus murallas. Conoce suelo, humedad, rotación, semillas, enfermedades vegetales y el momento en que una cosecha deja de poder salvarse mediante trabajo y empieza a convertirse en una pérdida económica.

                En las regiones integradas, la agricultura contemporánea dispone de agua saneada, transporte ferroviario próximo, herramientas de enorme precisión y conocimiento suficiente para caracterizar mediante respuesta vibracional alteraciones que generaciones anteriores sólo habrían reconocido cuando la planta comenzara a morir.

                A unas jornadas de distancia puede existir otro agricultor haciendo prácticamente el mismo trabajo con métodos heredados.

                No porque Valerian haya olvidado cómo ayudarlo.

                Su comarca simplemente quedó fuera del recorrido de aquello que justificaba hacerlo.

                La tecnología agrícola V881 no se distribuye siguiendo un mapa del conocimiento.

                Sigue un mapa de intereses.
                """);

        putSalary(m,Subprofession.LIVESTOCK_KEEPER,0.85,
                """
                Un animal sano continúa necesitando alimento todos los días.

                El ganadero cría y mantiene animales destinados a alimento, reproducción, trabajo, transporte y producción secundaria. Controla alimentación, parto, selección, comportamiento y aquellas pequeñas desviaciones que permiten llamar al veterinario antes de que una explotación completa descubra que comparte el mismo problema.

                La medicina frecuencial ha convertido numerosas enfermedades animales en fenómenos identificables con una precisión extraordinaria. Eso no vuelve irrelevante al hombre que observa diariamente al rebaño.

                Una frecuencia puede indicar qué ocurre.

                El ganadero suele ser quien advierte primero que algo está ocurriendo.

                En las zonas desarrolladas trabaja dentro de cadenas que conectan explotación, veterinario, transporte, mercado y ciudad. En las regiones periféricas puede seguir siendo simultáneamente productor, tratante, cuidador y primer recurso sanitario de sus animales.

                La diferencia tecnológica entre ambas explotaciones puede ser enorme.

                Los animales no lo saben.
                """);

        putSalary(m,Subprofession.HORTICULTURIST,0.90,
                """
                El horticultor trabaja con superficies menores y decisiones más frecuentes.

                Frutas, hortalizas, plantas medicinales, aromáticas y producciones sensibles a humedad, temperatura y calendario pueden valer más por unidad que una gran extensión de cereal y perderse mucho más deprisa cuando algo falla. Su oficio consiste en observar constantemente aquello que cambia antes de que el cambio resulte evidente para el comprador.

                En zonas V881 integradas combina riego saneado, selección de suelo, manejo fino y diagnóstico de alteraciones vegetales con una logística capaz de colocar productos perecederos en mercados lejanos con rapidez.

                En una comarca aislada puede disponer del mismo conocimiento y carecer de la estación, del comprador o del especialista que permitirían explotarlo plenamente.

                Por eso la horticultura enseña una de las reglas menos elegantes del progreso valeriano:

                producir bien no garantiza estar conectado con quien puede pagar por ello.
                """);


        putVariable(m,Subprofession.COASTAL_FISHER,0.85,
                """
                El pescador costero trabaja cerca de tierra y depende de ella más de lo que admite.

                Sale durante horas o una jornada, conoce fondos, mareas, temporadas y especies y regresa antes de que la conservación de la captura se convierta en el principal problema. Sus embarcaciones pueden beneficiarse de captación del viento, superficies solares y apoyo electroatmosférico sin necesitar la complejidad de un gran mercante.

                Su economía está ligada al puerto inmediato.

                Una buena captura vale poco si llega a un lugar sin compradores, hielo, conservación o caminos.

                Por eso los pueblos costeros bien conectados pueden vivir con notable comodidad mientras otros, apenas más lejos, siguen dependiendo de mercados estrechos y técnicas casi tradicionales.

                El mar ofrece recursos.

                La infraestructura decide cuánto de ese recurso termina convertido en renta.
                """);

        putVariable(m,Subprofession.OFFSHORE_FISHER,1.15,
                """
                El pescador de altura se aleja lo suficiente de la costa para que regresar deje de ser la respuesta inmediata a cualquier problema.

                Opera embarcaciones más autónomas, trabaja durante jornadas prolongadas y depende de navegación, conservación, mantenimiento y coordinación de tripulación. La captación eólica, solar y electroatmosférica reduce enormemente la necesidad de transportar combustible, pero no reduce la necesidad de agua, alimento, repuestos ni criterio.

                La recompensa potencial es mayor porque también lo son volumen de captura, distancia y riesgo.

                Una avería lejos de puerto no se vuelve menos seria porque el motor sea silencioso.

                La modernidad del barco cambia el tipo de dependencia.

                No elimina la dependencia del mar.
                """);

        putVariable(m,Subprofession.PROFESSIONAL_HUNTER,1.00,
                """
                El cazador profesional no sale al monte para demostrar que sabe matar.

                Sale porque alguien necesita aquello que puede traer de vuelta.

                Carne, pieles, trofeos Ferae, control de animales peligrosos, seguimiento y conocimiento territorial convierten su oficio en una mezcla de producción, servicio y gestión del riesgo. Debe reconocer huellas, estaciones, comportamiento y límites de una población antes de decidir cuánto puede extraerse sin destruir aquello de lo que depende su propia renta.

                Las rutas V881 han acercado mercados a territorios antes aislados, aumentando el valor de ciertos productos de caza.

                También han llevado compradores capaces de vaciar una zona con demasiada rapidez.

                El buen cazador sabe que vender todo hoy puede significar no tener nada que vender mañana.
                """);

        putVariable(m,Subprofession.TRAPPER,0.85,
                """
                El trampero trabaja con paciencia acumulada.

                Coloca, revisa y mantiene sistemas de captura, lee recorridos y aprende qué animal repite una ruta y cuál sólo pasó una vez. Su actividad exige menos persecución directa que la caza abierta y mucho más conocimiento de hábitos, terreno y tiempo.

                Puede trabajar por piel, carne, control de plagas o encargo.

                En regiones periféricas sigue siendo una fuente importante de recursos donde la logística moderna llega con dificultad. En zonas desarrolladas su trabajo puede volverse más especializado y regulado, especialmente cuando una especie tiene valor económico o simbólico.

                Una trampa parece inmóvil.

                El oficio consiste en entender todo lo que se mueve alrededor de ella.
                """);

        putSalary(m,Subprofession.FOREST_LUMBERJACK,0.90,
                """
                La madera empieza como árbol y termina formando parte de demasiadas cosas como para que alguien pueda fingir que el bosque es sólo paisaje.

                El leñador forestal tala, desrama, trocea, arrastra y clasifica madera destinada a construcción, carpintería, embalaje, herramientas y usos secundarios. Aprende a reconocer una pieza sana antes de invertir trabajo en bajarla y a distinguir cuánto puede extraerse de una zona sin convertir el siguiente año en un problema.

                Herramientas mejores han reducido parte del esfuerzo bruto.

                No han hecho ligera una masa de madera recién cortada.

                Su trabajo se concentra allí donde todavía existe recurso suficiente para justificar caminos, transporte y cuadrillas.

                Cuando el bosque útil retrocede o deja de ser rentable, el asentamiento que creció a su alrededor puede descubrir hasta qué punto dependía de algo que parecía permanente.
                """);

        putSalary(m,Subprofession.FORESTRY_MANAGER,1.10,
                """
                El gestor forestal trabaja con árboles que quizá no verá maduros.

                Planifica cortes, regeneración, accesos, protección frente a incendios, usos competidores y ritmo de extracción. Debe entender biología, suelo, agua, demanda industrial y el tiempo necesario para que una decisión económica de hoy no convierta el bosque de mañana en una fotografía.

                La expansión V881 aumentó la capacidad de transportar y procesar madera.

                Eso hizo más importante, no menos, decidir cuánto debía salir.

                Coordina leñadores, comerciantes, autoridades locales y propietarios y puede recomendar que una zona rentable permanezca intacta porque extraerla ahora destruiría una fuente futura de valor.

                Su trabajo es una forma de administración del tiempo.

                El bosque simplemente tarda más en responder que un mercado.
                """);

        putSalary(m,Subprofession.EXTRACTION_MINER,1.10,
                """
                Buena parte de la sofisticación V881 comienza en un lugar oscuro donde alguien sigue arrancando materia de la tierra.

                El minero perfora, sostiene, extrae, clasifica y transporta mineral bajo condiciones en las que un error estructural continúa siendo más rápido que cualquier medicina.

                Las técnicas modernas han mejorado ventilación, iluminación, bombeo, detección y herramientas. No han negociado con la masa de una montaña.

                Su salario superior al de muchos trabajadores manuales compensa parcialmente riesgo, especialización y desgaste.

                También explica por qué una región minera puede enriquecerse con extraordinaria rapidez.

                Y por qué puede empobrecerse con la misma violencia cuando aquello que justificaba excavar deja de compensar el coste de hacerlo.
                """);

        putVariable(m,Subprofession.PROSPECTOR,1.25,
                """
                El prospector puede cambiar el futuro de un pueblo sin construir una sola casa.

                Busca mineral, piedra, agua, materiales industriales y cualquier concentración natural cuyo aprovechamiento pueda justificar llevar hombres, capital e infraestructura hasta un lugar donde antes no había nada.

                Estudia terreno, muestras, estratos y señales superficiales antes de recomendar una exploración más costosa. Su informe puede preceder a mineros, comerciantes, ferrocarril, talleres, soldados y familias enteras.

                También puede precederlos en dirección contraria.

                Un yacimiento agotado no destruye físicamente la estación que ayudó a construir.

                Simplemente empieza a convertirla en una pregunta económica.

                Algunas poblaciones periféricas de Valerian fueron creadas porque alguien encontró algo bajo sus pies.

                Décadas después descubrieron que nadie había prometido permanecer cuando dejara de estar allí.
                """);

        putSalary(m,Subprofession.MERCHANT_SAILOR,1.10,
                """
                El ferrocarril domina la continuidad terrestre.

                El barco mercante domina aquello que no necesita permanecer sobre tierra.

                Los buques contemporáneos combinan captación directa de la energía del viento, velas solares y motores alimentados mediante energía atmosférica. Su autonomía energética puede resultar extraordinaria; su autonomía material no.

                Continúan necesitando tripulación, alimentos, mantenimiento, puertos, cartas, carga y hombres capaces de interpretar un mar que no ha firmado ningún acuerdo con V881.

                Transportan materias primas, productos manufacturados, pasajeros y maquinaria en volúmenes que ningún caballo, motocicleta o convoy improvisado podría justificar.

                La navegación moderna ha cambiado radicalmente la máquina.

                No ha cambiado el hecho fundamental de que, una vez desaparece la costa, quienes están a bordo tienen que bastarse durante un tiempo a sí mismos.
                """);


        putSalary(m,Subprofession.STEVEDORE,0.85,
                """
                Todo el comercio marítimo del mundo termina, durante unos minutos, siendo una caja que alguien tiene que mover.

                El estibador carga y descarga mercancías, organiza pesos, asegura bultos, reconoce marcas, trabaja con grúas y decide qué puede apilarse encima de qué sin convertir una bodega ordenada en una reclamación comercial.

                Convive con mercancías cuyo valor puede superar en una jornada todo lo que cobrará durante años.

                No son suyas.

                Esa proximidad entre riqueza y salario convierte los muelles en lugares extraordinariamente fértiles para comercio legítimo, pequeños hurtos, contrabando y redes capaces de conseguir que determinada caja aparezca registrada en un lugar ligeramente distinto de aquel donde terminó.

                La sofisticación del barco no elimina la economía del puerto.

                La concentra.
                """);

        putSalary(m,Subprofession.COMPANION_ANIMAL_BREEDER,0.90,
                """
                El criador de animales de compañía selecciona reproducción, temperamento, salud y socialización para producir animales capaces de convivir de forma estable con personas. No es un ganadero que maximiza carne, tiro o volumen, ni un cuidador que mantiene un ejemplar ya existente.

                Su trabajo exige observar generaciones, recordar parentescos, descartar cruces problemáticos y comprender señales de conducta antes de decidir qué animales deben reproducirse. Esa lectura enlaza de forma natural con EMPATÍA ANIMAL, pero no convierte la maestría en una orden mágica sobre la especie.
                """);

        putSalary(m,Subprofession.AGRICULTURAL_SELECTOR_CONDITIONER,0.68,
                """
                Entre cosechar un producto y poder venderlo existe otro trabajo.

                La seleccionadora y acondicionadora agrícola clasifica por tamaño, estado, madurez y destino; retira piezas dañadas, prepara lotes, envuelve, seca, limpia y ordena aquello que debe sobrevivir al transporte. Su rendimiento depende menos de mover gran masa que de repetir miles de decisiones pequeñas sin degradar el producto.

                Una mala selección puede arruinar una caja entera aunque la cosecha haya sido buena.
                """);

        putSalary(m,Subprofession.HAULAGE_LABORER,0.70,
                """
                El peón de acarreo existe porque ninguna obra termina donde empieza la cantera, el bosque o el almacén.

                Carga piedra, madera, sacos, herramientas y suministros entre cuadrillas; prepara animales de tiro, reparte pesos y aprende muy pronto cuánto puede moverse de una vez antes de convertir un transporte útil en una avería. No diseña el puente ni talla la piedra, pero sin él ambos oficios pierden horas esperando material.

                Es trabajo de fuerza, resistencia y juicio práctico sobre carga.
                """);

        putVariable(m,Subprofession.RURAL_AGGREGATOR,1.10,
                """
                El agricultor produce cien sacos.

                La ciudad no quiere negociar cien veces.

                El acopiador existe en medio.

                Compra producción dispersa, reúne volúmenes, almacena, clasifica, adelanta pagos cuando le conviene y organiza el transporte hacia mercados capaces de absorber cantidades que ningún productor individual podría colocar con eficiencia.

                Puede ser indispensable para una comarca.

                También puede convertirse en la persona que decide cuánto vale aquello que todos los demás necesitan vender antes de que se estropee.

                Su poder no procede necesariamente de poseer la tierra.

                Procede de controlar el punto donde muchas pequeñas producciones se convierten en una mercancía grande.

                Ésa es una forma de poder económico que Valerian conoce perfectamente.
                """);

        putVariable(m,Subprofession.CONVOY_ESCORT,1.25,
                """
                Las mercancías no desaparecen cuando abandonan una estación.

                A veces alguien intenta llevárselas.

                La escolta de convoy protege personas, animales, vehículos y carga durante rutas cuya importancia económica supera la capacidad inmediata del orden público para garantizar cada kilómetro. Puede acompañar caravanas rurales, transportes industriales, correo especializado o mercancías que atraviesan territorios donde la autoridad existe sobre el mapa con más claridad que sobre el camino.

                No sustituye al Soldado.

                Trabaja precisamente donde contratar protección resulta más rápido que esperar a que el Reino pueda proporcionarla de manera permanente.

                La profesión mercenaria prosperó después de la Primera Marcha Exaltada por problemas mucho mayores que éste.

                Pero pocos explican mejor por qué sigue siendo necesaria.
                """);



        //  — burguesía, Noble y arquitectura del poder V881.
        putVariable(m,Subprofession.V881_INDUSTRIALIST,2.50,
                """
                El Industrial V881 ya no gana principalmente porque una mercancía cambie de manos.

                Controla la capacidad de producirla.

                Talleres, maquinaria, personal especializado, contratos de suministro y acceso estable a materiales convierten una idea técnica en una serie de objetos que pueden llegar a existir más de una vez. Su riqueza aparece cuando deja de preguntar cuánto cuesta fabricar una pieza y empieza a decidir qué volumen de producción justifica mantener abierta una línea entera.

                Después de la Primera Marcha Exaltada, esa capacidad adquirió una dimensión política. Un industrial puede convertir una comarca en centro de empleo, atraer ferrocarril, crear demanda para viviendas y talleres auxiliares o retirar todo ello cuando producir en otro lugar resulte más conveniente.

                No gobierna necesariamente el territorio.

                A veces sólo decide dónde merece la pena seguir fabricando.

                Para quien vive allí, la diferencia puede resultar académica.
                """);

        putVariable(m,Subprofession.SHIPOWNER,3.00,
                """
                Un marinero trabaja sobre un barco.

                El armador decide cuántos barcos pueden trabajar.

                Posee, financia o administra buques mercantes y convierte capacidad naval en rutas, contratos, seguros, carga y calendario. Debe equilibrar mantenimiento, tripulación, puertos, riesgo, mercancías y una tecnología naval cuya enorme eficiencia energética no elimina ninguno de esos costes humanos.

                Captación del viento, velas solares y motores electroatmosféricos permiten que una nave dedique a carga una masa que otras ramas históricas habrían sacrificado en combustible.

                El armador convierte esa ventaja en capital.

                Su poder crece cuando varias ciudades dependen de que sus barcos lleguen a tiempo. Puede no producir una sola caja y, sin embargo, alterar el precio de miles sólo cambiando cuándo y dónde aparece su bodega.

                El océano pertenece a todos.

                La capacidad de mover una parte significativa de lo que lo cruza, no.
                """);

        putVariable(m,Subprofession.FINANCIER,2.75,
                """
                El financista comercia con tiempo antes que con objetos.

                Presta, adelanta, asegura, participa, concentra ahorro y decide qué proyecto puede empezar hoy utilizando riqueza que todavía no ha producido. Una estación, un taller o una compañía mercenaria pueden poseer conocimiento, hombres y demanda y seguir sin existir hasta que alguien acepte financiar el intervalo entre gastar y recuperar.

                Su actividad parece abstracta únicamente mientras funciona.

                Cuando retira crédito, exige garantías o considera que una región ya no merece riesgo, las consecuencias se vuelven extremadamente físicas: una obra se detiene, una empresa vende equipo, una familia pierde patrimonio o un corredor comercial deja de crecer.

                La burguesía V881 no necesita gobernar cada ciudad para influir sobre su futuro.

                Le basta con decidir qué futuros considera financiables.
                """);

        putVariable(m,Subprofession.INFRASTRUCTURE_CONCESSIONAIRE,3.50,
                """
                Hay personas cuya mercancía es el derecho a construir aquello que todos los demás terminarán utilizando.

                El concesionario de infraestructura negocia explotación, mantenimiento y rentabilidad de estaciones, corredores ferroviarios, instalaciones electroatmosféricas, puertos, redes técnicas y otros sistemas cuya escala excede el taller de un solo propietario. Coordina capital privado, autoridad, juristas, maestros, industriales y trabajadores.

                Su decisión puede conectar una región durante generaciones.

                También puede dejarla fuera.

                Una vía no se curva hacia cada población que la desea. Un captador no se instala donde nadie espera recuperar su coste. Un puerto no mantiene capacidad ilimitada por cortesía hacia una comarca que ya no produce nada estratégico.

                Por eso parte de la desigualdad territorial de Valerian puede rastrearse hasta decisiones que, sobre el papel, sólo parecían cálculos de rentabilidad.

                El mapa tecnológico del Reino es también un mapa de concesiones.
                """);

        putVariable(m,Subprofession.GRAND_MERCHANT,2.25,
                """
                El Gran comerciante no necesita poseer una fábrica, una flota ni una mina para tratar con quienes sí las poseen.

                Opera volúmenes, rutas y relaciones capaces de conectar varias cadenas económicas al mismo tiempo. Compra donde una mercancía todavía es producción y vende donde ya se ha convertido en escasez. Puede combinar acopio rural, transporte marítimo, corredores ferroviarios, almacenamiento urbano y crédito hasta que la diferencia entre comerciante y organizador económico empieza a volverse incómoda.

                Su ventaja principal es la información.

                Sabe qué falta antes de que el consumidor lo perciba, qué sobra antes de que el productor lo admita y qué región puede quedar aislada si una ruta cambia.

                La riqueza nueva posterior a la Primera Marcha Exaltada encontró en figuras como ésta una vía para competir con patrimonios mucho más antiguos.

                No heredaron necesariamente el mundo.

                Aprendieron a interponerse entre sus partes.
                """);

        putVariable(m,Subprofession.MERCENARY_COMPANY_DIRECTOR,3.00,
                """
                Una compañía mercenaria deja de ser un grupo de hombres armados cuando alguien consigue que continúe existiendo entre contrato y contrato.

                El Director de compañía reúne combatientes, artesanos, técnicos, transportistas, suministros, crédito y relaciones políticas suficientes para vender capacidad organizada. Puede aceptar protección, construcción, recuperación, campaña, mantenimiento o control temporal de una zona y distribuir después el trabajo entre especialistas que jamás habrían podido ofertar por separado la misma escala de servicio.

                Después de la Primera Marcha Exaltada algunas compañías crecieron hasta convertirse en centros de poder territorial.

                Sus directores podían negociar con comerciantes, autoridades y nobles, sostener hombres durante meses y decidir qué violencia merecía convertirse en contrato.

                De ahí nacieron auténticos señores de la guerra mercenarios.

                No porque poseyeran una categoría sobrenatural de fuerza.

                Porque llegaron a administrar suficientes personas, recursos y armas como para que su voluntad tuviera geografía.
                """);

        putRent(m,Subprofession.DYNASTIC_NOBLE,210000,
                """
                El Noble de sangre hereda antes de producir.

                Su posición procede de patrimonio, derechos, tierras, relaciones y continuidad dinástica capaces de sobrevivir a una vida individual. La Primera Marcha Exaltada destruyó linajes enteros y redujo otros a apellidos sin fortuna, pero quienes conservaron suficiente estructura salieron del periodo de reconstrucción con una ventaja imposible de reproducir mediante un salario ordinario.

                La renta de un Berylare mensual es sólo una referencia de clase, no un techo.

                Un noble puede vivir rodeado de tecnología V881 sin fabricar ninguna de sus piezas y ejercer poder sin aparecer en la cadena formal que ejecuta una decisión.

                Su verdadero privilegio no consiste en no trabajar.

                Consiste en poder esperar.

                Cuando la vida, el capital y la institución favorecen al mismo individuo durante décadas, el tiempo deja de comportarse como una presión igual para todos.
                """);

        putRent(m,Subprofession.CONCESSIONARY_NOBLE,315000,
                """
                El Noble concesionario convierte patrimonio en territorio funcional.

                Posee o controla derechos sobre recursos, rutas, instalaciones, explotaciones y permisos cuya importancia permite orientar desarrollo sin necesidad de administrar directamente cada calle. Puede asociarse con industriales y comerciantes, conceder acceso, financiar una conexión o bloquearla hasta que las condiciones resulten más favorables.

                Después de la Primera Marcha Exaltada este poder aumentó porque V881 hizo que determinadas infraestructuras valieran mucho más que el suelo desnudo sobre el que se levantaban.

                Una población puede creer que está discutiendo si merece una estación.

                El noble puede estar discutiendo si la estación altera el precio de tres recursos, desplaza mano de obra hacia una comarca rival o vuelve innecesaria una ruta sobre la que ya posee derechos.

                La escala de la conversación cambia según quién pueda permitirse mirar el mapa entero.
                """);

        putRent(m,Subprofession.ENLIGHTENED_PATRON,262500,
                """
                El Mecenas ilustrado financia conocimiento y decide qué clase de conocimiento merece convertirse en institución.

                Laboratorios, maestros, expediciones, publicaciones, hospitales, archivos, investigación frecuencial y proyectos V881 pueden prosperar durante años gracias a una sola casa patrimonial. El gesto puede ser sinceramente generoso. También puede orientar una disciplina entera sin necesidad de censurarla explícitamente.

                Su figura encaja de manera natural con la Institución de la Esfera del Progreso.

                El Humanismo Secular ofrece una justificación intelectual formidable para quienes han visto a la humanidad dominar enfermedad, energía, locomoción y longevidad. Financiar ese progreso puede parecer no sólo útil, sino moralmente evidente.

                La dificultad aparece cuando aquello que no cabe dentro de la Esfera deja de recibir recursos, prestigio o lenguaje respetable.

                No hace falta prohibir una idea para hacer que una generación de investigadores aprenda a no formularla.
                """);

        putRent(m,Subprofession.PATRIMONIAL_WARLORD,367500,
                """
                El Señor de guerra patrimonial no necesita ponerse una armadura para disponer de fuerza militar.

                Financia compañías, mantiene arsenales, compra lealtades, asegura rutas y puede sostener violencia durante más tiempo del que un adversario puede sostener resistencia. Su patrimonio convierte soldados y mercenarios en una capacidad que existe antes de que aparezca un enemigo concreto.

                La guerra V881 es demasiado barata en destrucción directa para explicarse únicamente por la necesidad de vencer en batalla.

                Un conflicto puede mover población, modificar precios, probar diseños, crear o destruir mercados, justificar concesiones y revelar quién obedece cuando obedecer deja de ser cómodo.

                El Señor de guerra patrimonial opera en esa escala.

                Puede ganar sin conquistar una ciudad.

                A veces basta con que, después del conflicto, la ciudad necesite comprarle algo que antes no necesitaba.
                """);



        //  — élite epistemológica: continuidad, alma, metamorfosis, permanencia e Intersticio.
        putSalary(m,Subprofession.REGENERATIONIST,2.20,
                """
                El Regeneracionista trabaja donde curar deja de significar simplemente eliminar una enfermedad.

                Su especialidad es restaurar un organismo hacia estados de coherencia previamente registrados y mantener esa continuidad durante períodos que la medicina ordinaria nunca tuvo que contemplar. No confunde regeneración con rejuvenecimiento: sabe que cada restauración parte del cuerpo que existe ahora, con toda la historia biológica que ya ha acumulado, y no de una plantilla humana abstracta e intacta.

                Por eso su trabajo exige reconstruir qué parte de una alteración pertenece a una lesión reciente y qué parte forma ya parte estable de la persona que intenta conservar. Una intervención demasiado agresiva puede borrar adaptación útil; una demasiado conservadora puede fijar daño acumulado.

                La élite recurrió a esta disciplina para convertir enfermedad, degeneración y oxidación en problemas aplazables.

                El Regeneracionista fue quien descubrió el precio.

                La continuidad puede prolongarse durante siglos.

                La historia también.
                """);

        putSalary(m,Subprofession.CONTINUITY_EPIGENETICIST,2.35,
                """
                El Epigenetista de continuidad estudia aquello que permanece después de que un cuerpo haya sido reparado demasiadas veces.

                Compara linajes celulares, expresión, memoria epigenética, respuesta regenerativa y desviaciones que sólo se vuelven visibles cuando varias décadas de restauración se observan como una sola serie. No busca una mutación espectacular. Busca diferencias pequeñas que dejan de ser pequeñas después de repetirse durante generaciones de tejido.

                Su disciplina demostró que la regeneración frecuencial no devuelve a un organismo a una versión original.

                Devuelve coherencia a una versión históricamente acumulada.

                Ese matiz separa longevidad de estabilidad.

                También explica por qué determinados individuos muestran una plasticidad corporal que ningún médico corriente debería esperar. La biomáquina basada en carbono-12 conserva suficiente capacidad de cambio para que siglos de cicatrización, presión y adaptación terminen expresándose en anatomías que ya no parecen variaciones ordinarias de una persona.

                Quien estudia esa deriva conoce la base material de Cambiaformas mucho antes de conocer a alguien que admita poseerla.
                """);

        putSalary(m,Subprofession.NEUROARCHITECT,2.40,
                """
                El Neuroarquitecto estudia identidad en el lugar donde los recuerdos dejan de ser una lista y se convierten en estructura.

                Mapea conectividad, plasticidad, hábitos, respuestas emocionales, automatismos, aprendizaje y patrones de activación que permiten reconocer a una persona incluso cuando sus tejidos se han regenerado innumerables veces. El ADN puede describir posibilidades biológicas. No contiene por sí solo la biografía neuronal que convirtió esas posibilidades en un individuo.

                Su trabajo se volvió imprescindible cuando la longevidad extrema empezó a producir cerebros que habían acumulado siglos de experiencia y adaptación.

                También reveló algo más incómodo.

                Una arquitectura neuronal sometida durante siglos a miedo, dominio, depredación, vigilancia, territorialidad, deseo o violencia puede adquirir patrones tan extremos que el cuerpo regenerado empieza a acomodarse a ellos.

                Para un Neuroarquitecto, una anatomía monstruosa no tiene por qué ser un accidente.

                Puede ser una autobiografía.
                """);

        putSalary(m,Subprofession.SOUL_RESEARCHER,2.50,
                """
                El Investigador álmico estudia el componente de identidad que ni el cuerpo ni la arquitectura neuronal consiguen explicar por separado.

                No trabaja con fe.

                Trabaja con coherencia.

                Observa cómo determinadas firmas permanecen acopladas a un organismo, cómo reaccionan ante restauración, qué sucede cuando cuerpo y memoria neuronal dejan de coincidir y bajo qué condiciones una continuidad puede reconocerse como la misma persona a pesar de haber cambiado materialmente.

                La Institución de la Esfera del Progreso tolera esta disciplina porque sus resultados son medibles.

                No necesita aceptar al Santo, al Padre de Todos ni una cosmología religiosa para admitir que existe una variable que desaparece cuando el individuo deja de estar presente y que no puede reducirse satisfactoriamente a tejido.

                El investigador álmico sabe que llamar alma a esa variable no la vuelve menos científica.

                Lo peligroso empieza cuando alguien decide que, si puede medirse, quizá también pueda trasladarse.
                """);

        putSalary(m,Subprofession.SOUL_TRANSFUSIONIST,2.75,
                """
                El Trasvasista intenta trasladar continuidad personal a un soporte biológico distinto.

                Es una de las especialidades más restringidas de Valerian porque sus fracasos son difíciles de confundir con un simple error médico. Un receptor puede rechazar la continuidad, conservar fragmentos incompatibles, perder memoria, destruir parte de su arquitectura previa o sobrevivir con una identidad cuya estabilidad ya no admite una respuesta sencilla.

                El procedimiento no reinicia a nadie.

                Trasladar un alma no elimina la historia que la acompaña ni garantiza que un nuevo cuerpo pueda reconciliarla con una arquitectura neuronal diferente. Cuanto más antigua y enmarañada es la persona, más difícil resulta encontrar un soporte capaz de recibirla sin violencia.

                Por eso el trasvase nunca sustituyó a la regeneración como segunda vía limpia hacia la inmortalidad.

                Algunos de los individuos que más necesitaban escapar de su cuerpo eran precisamente aquellos que peor cabían en otro.
                """);

        putSalary(m,Subprofession.SILICIC_METAMORPHOSIS_RESEARCHER,3.10,
                """
                El Investigador de metamorfosis silícica parte de una conclusión que toda la medicina regenerativa intentó evitar durante siglos:

                el problema no puede resolverse reparando mejor el mismo material.

                La matriz orgánica de carbono-12 es extraordinariamente adaptable y precisamente por ello acumula deriva. La metamorfosis silícica intenta sustituir progresivamente ese régimen mutable por una arquitectura material capaz de conservar la configuración alcanzada sin repetir indefinidamente la misma cicatrización.

                No busca convertir a una persona en una estatua mineral.

                Busca que la identidad deje de depender de una biomáquina obligada a reinterpretarse cada vez que se restaura.

                Es una disciplina casi inaccesible incluso para la élite. Exige comprender regeneración, arquitectura neuronal, coherencia álmica, materiales y estabilidad de una transición que no admite un ensayo completo sobre el mismo individuo dos veces.

                Quienes financian este trabajo no suelen hacerlo por curiosidad.

                Tienen un reloj.
                """);

        putSalary(m,Subprofession.PERMANENCE_RESEARCHER,3.20,
                """
                El Investigador de permanencia estudia aquello que empieza después de que sobrevivir deje de ser la pregunta principal.

                Si una identidad puede estabilizarse materialmente, todavía queda por determinar qué relación mantiene con aquello que la rodea. Algunas configuraciones dejan correlaciones persistentes en lugares, estructuras, objetos o fenómenos incluso cuando el cuerpo que las originó ya no ocupa el mismo punto.

                El investigador distingue longevidad, inmortalidad material y permanencia.

                La primera prolonga una vida.

                La segunda estabiliza el soporte.

                La tercera permite que el patrón de una identidad deje de depender por completo de un único cuerpo localizado.

                Ese último paso cambia la escala del problema.

                Una persona deja de ser únicamente algo que puede encontrarse en una habitación y empieza a convertirse en algo cuya presencia puede inferirse por los efectos que persisten fuera de ella.

                Muy pocos investigadores llegan lo bastante lejos como para estudiar esos efectos sin terminar trabajando junto a un Ilustrado.
                """);

        putSalary(m,Subprofession.ENLIGHTENED,3.50,
                """
                El Ilustrado estudia la realidad cuando deja de comportarse como una superficie continua.

                Su disciplina comienza donde terminan permanencia, resonancia y topología del Intersticio. Observa líneas telúricas, hendiduras des-veladas, alteraciones del filtro mediante el cual se proyecta la realidad y fenómenos en los que distancia, presencia y percepción dejan de obedecer exactamente a la geometría ordinaria.

                No llama al Velo un mundo de espíritus.

                Un Velo es una hendidura del Intersticio.

                Atravesarlo puede equivaler a recorrer una continuidad telúrica que desde fuera parece teletransporte o a proyectar la misma realidad mediante un filtro diferente hasta que aquello conocido se presenta con otra configuración.

                El Panóptico del Ilustrado recibe su nombre de esta tradición. Su arquitectura de observación no es un adorno honorífico: resume la obsesión profesional por reunir perspectivas incompatibles antes de decidir qué parte de una escena era realmente visible.

                Un Ilustrado puede dedicar años a estudiar un lugar que otros atraviesan todos los días.

                La diferencia es que él sabe que quizá nunca estuvieron atravesando exactamente el mismo lugar.
                """);

        putSalary(m,Subprofession.CONTINUITY_JURIST,2.20,
                """
                El Jurista de continuidad trabaja con una pregunta que la medicina hizo inevitable:

                ¿cuándo sigue siendo jurídicamente la misma persona alguien que ha sobrevivido demasiado tiempo?

                Contratos, matrimonio, propiedad, responsabilidad, herencia, representación, capacidad y deuda fueron concebidos para vidas que terminaban antes de que cuatro generaciones tuvieran que discutir con el mismo firmante. La regeneración extrema convirtió esas hipótesis en problemas cotidianos para determinados patrimonios.

                El trasvase añade otra dificultad. Una persona con lagunas de memoria puede conservar obligaciones que ya no recuerda. Un soporte corporal distinto puede seguir reclamando una identidad anterior. Una alteración neuronal puede dejar intacta la firma jurídica mientras cambia radicalmente la voluntad que la ejerce.

                El Jurista de continuidad no decide qué es un alma.

                Decide qué consecuencias legales produce afirmar que sigue siendo la misma.

                En una sociedad que puede prolongar a sus élites durante siglos, esa distinción vale fortunas.
                """);

        putSalary(m,Subprofession.DOCTRINE_CUSTODIAN,2.45,
                """
                El Custodio de doctrina administra la frontera entre conocimiento verdadero y conocimiento socialmente utilizable.

                Clasifica informes, determina qué hallazgos pueden entrar en publicaciones, qué vocabulario resulta compatible con la Institución de la Esfera del Progreso y qué descubrimientos deben permanecer restringidos por seguridad, estabilidad institucional o interés estratégico.

                No necesita quemar libros.

                Una disciplina puede desaparecer del debate público simplemente dejando de recibir acceso, financiación, lenguaje respetable y credenciales.

                Su trabajo explica cómo Valerian puede medir alma, Intersticio y anomalías de continuidad sin que la población abandone el Humanismo Secular ni sospeche que los relatos antiguos quizá describían algo más que superstición.

                El mejor secreto no siempre es aquello que nadie ha visto.

                A veces es aquello que todo el mundo ha aprendido a describir de una manera que impide reconocerlo.
                """);

        putSalary(m,Subprofession.FREQUENCY_INSTRUMENT_MAKER,2.00,
                """
                El Instrumentista frecuencial construye la parte de la ciencia que no cabe en una teoría.

                Osciladores, resonadores, captadores, reguladores, cámaras de tratamiento, sensores, aislamiento y mecanismos de calibración deben producir respuestas reproducibles antes de que un Maestro pueda atribuir significado a una frecuencia. Una desviación mínima puede convertir una lectura clínica en ruido o una restauración delicada en un fracaso.

                Su oficio pertenece al Herrero porque sigue dependiendo de material, tolerancia, unión, ajuste y reparación.

                La diferencia es que trabaja con máquinas cuya precisión debe conservarse también como comportamiento vibratorio.

                Los laboratorios elitistas suelen proteger a sus mejores instrumentistas casi tanto como a los investigadores.

                Una teoría puede copiarse en papel.

                La mano capaz de construir correctamente aquello que la prueba tarda mucho más en reproducirse.
                """);

        putSalary(m,Subprofession.MATRIX_ARCHITECT,2.60,
                """
                El Arquitecto de matrices fabrica soportes destinados a mantener configuraciones que la materia ordinaria no conservaría con suficiente fidelidad.

                Trabaja con materiales V881, geometrías de precisión, interfaces biológicas y estructuras capaces de participar en experimentos de regeneración avanzada, trasvase o metamorfosis silícica. No decide qué identidad debe ocupar una matriz.

                Decide si la matriz puede sostener aquello que otro pretende hacerle cargar.

                Una pieza destinada a un proyecto de permanencia puede requerir tolerancias, pureza y estabilidad que vuelven inútil casi cualquier componente comercial.

                Por eso sus talleres producen muy poco y rechazan muchísimo.

                El coste de una matriz no se explica sólo por el material que contiene.

                Se explica por todo el material que tuvo que descartarse antes de obtener uno que dejara de introducir su propia historia en el experimento.
                """);

        putVariable(m,Subprofession.EXCEPTIONAL_ASSET_RECOVERER,2.25,
                """
                Hay encargos que no pueden describirse como comprar, escoltar o robar sin perder parte de lo que realmente exigen.

                El Recuperador de activos excepcionales localiza y devuelve individuos, muestras, artefactos, documentos o materiales cuya rareza vuelve irrelevante el mercado ordinario. Puede trabajar para una casa noble, una institución, un laboratorio o una compañía que ni siquiera le explica por qué aquello que busca importa.

                Su tarea empieza donde termina la cadena de suministro.

                Un objeto puede hallarse en una ruina, una persona puede no querer ser encontrada y un material puede existir únicamente en un lugar que nadie controla de manera estable.

                No todos los recuperadores conocen el destino final de sus encargos.

                Los mejores aprenden a reconocer cuándo la paga incluye precisamente no preguntar.
                """);

        putSalary(m,Subprofession.STRATEGIC_INSTALLATION_CUSTODIAN,1.65,
                """
                El Custodio de instalación estratégica protege lugares cuya importancia real no coincide necesariamente con aquello que figura en la puerta.

                Laboratorios, archivos, estaciones de tratamiento, depósitos, cámaras de ensayo y centros de investigación pueden parecer instalaciones técnicas ordinarias hasta que alguien intenta entrar donde no debe. El Custodio controla accesos, perímetros, traslados y respuesta armada sin necesitar comprender cada experimento que protege.

                Su disciplina es deliberadamente compartimentada.

                Sabe qué puerta no debe abrirse.

                Puede no saber qué existe detrás.

                Esta separación permite que un Estado o una casa noble mantenga conocimiento extraordinariamente restringido sin exigir que cada soldado encargado de protegerlo se convierta también en conspirador.

                La ignorancia organizada puede ser una medida de seguridad.
                """);

        putVariable(m,Subprofession.RESTRICTED_MATERIALS_BROKER,2.40,
                """
                El Corredor de materiales restringidos comercia con cosas que no aparecen en una tienda aunque alguien pueda ponerles precio.

                Materiales de pureza excepcional, componentes experimentales, matrices, muestras biológicas, instrumental, piezas recuperadas y sustancias sujetas a autorización circulan por contratos donde comprador y vendedor importan tanto como la mercancía.

                Su función no es crear escasez.

                Es administrarla.

                Conoce qué laboratorio puede utilizar un material, qué autoridad puede autorizarlo y qué propietario aceptará desprenderse de él únicamente si recibe algo que no se expresa en Valeritas.

                A medida que determinados proyectos de continuidad se acercan a sus límites, ese mercado adquiere una presión distinta.

                Hay recursos que pueden reemplazarse con dinero.

                Otros existen tan pocas veces que el dinero sólo decide quién empieza a buscarlos primero.
                """);

        putRent(m,Subprofession.PERMANENCE_PRETENDER,420000,
                """
                El Pretendiente a la Permanencia ya conoce el problema completo.

                Sabe que la regeneración no ofrece eternidad, que el trasvase no reinicia la identidad y que la biomáquina de carbono-12 terminará pagando siglos de mutabilidad acumulada. Conoce clases, marcas rúnicas y maestrías como propiedades reales del sistema y entiende que la metamorfosis silícica exige condiciones que ningún patrimonio puede comprar de forma directa.

                Por eso su riqueza deja de servir para adquirir comodidad.

                Sirve para fabricar circunstancias.

                Financia regeneracionistas, neuroarquitectos, trasvasistas, Ilustrados, recuperadores, compañías y laboratorios. Compra tiempo, conocimiento, territorio y fricción mientras busca aquello que todavía no ha conseguido producir a voluntad: mucus negruzco y un individuo capaz de alcanzar los umbrales extremos de VITALIDAD y ADAPTABILIDAD requeridos para completar la transición.

                Algunos Pretendientes ya intentaron enfrentarse a su doppelgänger.

                No lo derrotaron.

                Convergieron con él hasta que ambos dejaron de ser separables.

                Desde entonces comprenden con especial claridad que poder, longevidad y conocimiento no bastan.

                Se aproximan al límite de su propio cuerpo mientras intentan crear en otros las condiciones que quizá les enseñen cómo escapar del suyo.
                """);

        //  — nuevas especialidades sexuadas de Soldado/Mercenario.
        putSalary(m,Subprofession.V881_CAMPAIGN_SAPPER,1.35,
                "Zapador de campaña V881: movilidad, apertura de rutas, demolición, apuntalamiento y reparación bajo amenaza.");
        putSalary(m,Subprofession.V881_HEAVY_WEAPONS_SPECIALIST,1.55,
                "Especialista institucional en plataformas pesadas, munición de gran volumen y apoyo directo.");
        putSalary(m,Subprofession.INSTITUTIONAL_SHOCK_COMBATANT,1.50,
                "Combatiente institucional especializado en choque, ruptura y armas cuerpo a cuerpo de alta exigencia.");
        putSalary(m,Subprofession.V881_SUPPORT_MARKSWOMAN,1.35,
                "Tiradora de apoyo V881: precisión, observación, discriminación de blancos y apoyo remoto.");

        putVariable(m,Subprofession.CONTRACTUAL_SHOCK_COMBATANT,1.85,
                "Combatiente de choque contractual para encargos de ruptura y recuperación con armamento especializado.");
        putVariable(m,Subprofession.FRONTIER_SKIRMISHER,1.30,
                "Hostigadora de frontera dedicada a reconocimiento, tiro no convencional y control de distancia.");
        putVariable(m,Subprofession.MOBILE_ESCORT,1.45,
                "Escolta móvil para protección inmediata de personas y convoyes mediante respuesta rápida.");
        putVariable(m,Subprofession.TECHNICAL_RECOVERY_OPERATOR,1.90,
                "Operadora de recuperación técnica para localizar, asegurar y extraer activos con soporte instrumental.");
        putVariable(m,Subprofession.SABOTAGE_DENIAL_SPECIALIST,1.75,
                "Especialista de sabotaje y negación para impedir uso de infraestructura, crear ventanas de retirada y neutralizar recursos.");

        putSalary(m,Subprofession.V881_INDUSTRIAL_CONTRACT_AGENT,1.65,
                "Intermedia contratos de suministro industrial, traduce requisitos técnicos a condiciones comerciales y mantiene relaciones con talleres y compradores sin administrar directamente la fábrica.");
        putSalary(m,Subprofession.V881_INDUSTRIAL_CONSULTANT,1.90,
                "Acompaña a instalaciones industriales que ya existen y decide qué capacidad, repuesto o reorganización permite que sigan siendo viables. Su trabajo consiste en diagnosticar cadenas de producción, no en poseerlas.");

        putRent(m,Subprofession.STRATEGIC_COMMUNICATIONS_OFFICER,185000,"""
                Llegó a la nobleza aprendiendo que un ejército, una frontera o una concesión extensa pueden fracasar por una orden que no llega. Convirtió geometría, meteorología, observación y disciplina de señales en una infraestructura humana capaz de coordinar territorio sin depender siempre de una red fija. Su Espejo heliográfico V881 conserva el lenguaje material de las campañas que hicieron su reputación.
                """);
        putRent(m,Subprofession.FORENSIC_INVESTIGATOR,190000,"""
                Su patrimonio procede de resolver aquello que personas poderosas necesitaban mantener sin respuesta. Reconstruye escenas, conserva imágenes, contrasta tiempo y posición y somete alimentos, tejidos y fluidos a toxicología. El Nocturlabio V881, la cámara, el Contenedor Stas-Otto y el Aparato de Marsh pertenecen a una misma disciplina: separar lo ocurrido de lo que alguien necesita que parezca haber ocurrido.
                """);
        putRent(m,Subprofession.INTELLIGENCE_AGENT,195000,"""
                Ascendió obteniendo información mientras todavía podía alterar una decisión. Espionaje y contraespionaje le enseñaron a tratar una vibración, una ausencia y una respuesta resonante como indicios antes de convertirlos en acusaciones. El Sismoscopio y el Diapasón V881 preservan la ventaja de saber sin revelar necesariamente que se sabe.
                """);
        putRent(m,Subprofession.FIELD_ELECTROATMOSPHERIC_SPECIALIST,200000,"""
                Llevó la electroatmósfera fuera de torres y laboratorios. Aprendió a leer cuándo cielo, terreno y una instalación improvisada permiten trabajar y cuándo una descarga convertiría al operador en parte del circuito. El Tokkosho V881 resume esa trayectoria: un instrumento pequeño cuya letalidad sólo existe cuando el entorno físico permite completar su función.
                """);

        if(m.size()!=Subprofession.values().length)
            throw new IllegalStateException("Toda subprofesión debe tener perfil.");
        return m;
    }

    private static void putSalary(EnumMap<Subprofession,SubprofessionProfile> m,Subprofession s,double sueldos,String narrative){
        put(m,s,ProfessionIncomeKind.SALARY,sueldos,narrative,false,Optional.empty());
    }

    private static void putVariable(EnumMap<Subprofession,SubprofessionProfile> m,Subprofession s,double sueldos,String narrative){
        put(m,s,ProfessionIncomeKind.VARIABLE_INCOME,sueldos,narrative,false,Optional.empty());
    }

    private static void putUnique(EnumMap<Subprofession,SubprofessionProfile> m,Subprofession s,double sueldos,String holder,String narrative){
        put(m,s,ProfessionIncomeKind.SALARY,sueldos,narrative,true,Optional.of(holder));
    }

    private static void putNone(EnumMap<Subprofession,SubprofessionProfile> m,Subprofession s,String narrative){
        m.put(s,new SubprofessionProfile(s,ProfessionIncomeKind.NONE,0,"0 Valeritas/mes",
                narrative.strip(),false,Optional.empty()));
    }

    private static void putRent(EnumMap<Subprofession,SubprofessionProfile> m,Subprofession s,int valeritas,String narrative){
        double berylares=(double)valeritas/ProfessionProfileCatalog.VALERITAS_PER_BERYLARE;
        String label=(Math.abs(berylares-Math.rint(berylares))<1e-9)
                ? ((int)Math.rint(berylares))+" Berylare/mes"
                : String.format(java.util.Locale.ROOT,"%.2f Berylares/mes",berylares).replace('.',',');
        m.put(s,new SubprofessionProfile(s,ProfessionIncomeKind.PATRIMONIAL_RENT,valeritas,label,
                narrative.strip(),false,Optional.empty()));
    }

    private static void put(EnumMap<Subprofession,SubprofessionProfile> m,Subprofession s,ProfessionIncomeKind kind,
                            double sueldos,String narrative,boolean unique,Optional<String> holder){
        int valeritas=(int)Math.round(sueldos*ProfessionProfileCatalog.VALERITAS_PER_SUELDO);
        String label=String.format(java.util.Locale.ROOT,"%.2f Sueldos/mes",sueldos).replace('.',',');
        m.put(s,new SubprofessionProfile(s,kind,valeritas,label,narrative.strip(),unique,holder));
    }
}
