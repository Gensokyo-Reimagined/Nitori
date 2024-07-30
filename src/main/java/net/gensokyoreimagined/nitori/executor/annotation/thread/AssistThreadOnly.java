package net.gensokyoreimagined.nitori.executor.annotation.thread;

//import java.lang.annotation.Documented;
//import java.lang.annotation.ElementType;
//import java.lang.annotation.Target;
//
///**
// * An annotation primarily for methods, identifying methods that can only be called on a thread that is an instance
// * of {@link AssistThread}.
// * <br>
// * This annotation can also be used on fields or classes, similar to {@link ThreadRestricted}.
// * <br>
// * In a method annotated with {@link AssistThreadOnly}, fields and methods annotated with
// * {@link AssistThreadOnly}, {@link BaseThreadOnly} or {@link AnyThreadSafe} may be used.
// * <br>
// * Methods that are annotated with {@link AssistThreadOnly} must never call methods that are annotated with
// * {@link PotentiallyBlocking}.
// *
// * @author Martijn Muijsers under AGPL-3.0
// */
//@SuppressWarnings("unused")
//@Documented
//@Target({ElementType.METHOD, ElementType.TYPE, ElementType.FIELD})
//public @interface AssistThreadOnly {
//
//    /**
//     * @see ThreadRestricted#fieldAccess()
//     */
//    Access value() default Access.READ_WRITE;
//
//}