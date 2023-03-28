/*
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package net.TheDgtl.Stargate;

import net.TheDgtl.Stargate.Stargate;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Level;

/**
 * An enum containing various new features that might be available
 */
public enum NonLegacyMethod {
    
    /**
     * Fetching 1.16 Dye-Based Sign Colors
     *
     * <p>This ESR supports pre-1.16 versions.</p>
     */
    GET_SIGN_COLOR("org.bukkit.block.Sign", "getColor"),
    
    /**
     * Setting 1.16 Dye-Based Sign Colors
     *
     * <p>This ESR supports pre-1.16 versions.</p>
     */
    SET_SIGN_COLOR("org.bukkit.block.Sign", "setColor"),
    
    /**
     * Interacting with END_GATEWAYS
     * 
     * <p> This ESR supports pre-1.9 versions.</p>
     */
    REAGE_END_GATEWAY("org.bukkit.block.EndGateway", "setAge");

    private String classToCheckFor;
    private String methodInClassToCheckFor;
    private Class<?>[] parameters;
    private boolean isImplemented;

    /**
     * Instantiates a new non-legacy method
     *
     * @param classToCheckFor         <p>The class containing the method</p>
     * @param methodInClassToCheckFor <p>The legacy method itself</p>
     * @param parameterTypes          <p>The parameters expected by the method</p>
     */
    NonLegacyMethod(String classToCheckFor, String methodInClassToCheckFor, Class<?>... parameterTypes) {
        try {
            Class<?> aClass = Class.forName(classToCheckFor);
            aClass.getMethod(methodInClassToCheckFor, parameterTypes);
            this.classToCheckFor = classToCheckFor;
            this.methodInClassToCheckFor = methodInClassToCheckFor;
            this.parameters = parameterTypes;
            isImplemented = true;
        } catch (ClassNotFoundException | NoSuchMethodException ignored) {
            isImplemented = false;
        }
    }

    /**
     * Checks whether this non-legacy method is available
     *
     * @return <p>Whether this non-legacy method is available</p>
     */
    public boolean isImplemented() {
        return isImplemented;
    }

    /**
     * Invokes this non-legacy method
     *
     * @param object     <p>The object to invoke the method on</p>
     * @param parameters <p>The parameters required for the method</p>
     * @return <p>The return value of the invocation</p>
     */
    @SuppressWarnings("UnusedReturnValue")
    public Object invoke(Object object, Object... parameters) {
        try {
            Class<?> aClass = Class.forName(classToCheckFor);
            Method method = aClass.getMethod(methodInClassToCheckFor, this.parameters);
            return method.invoke(object, parameters);
        } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException |
                 IllegalAccessException e) {
            return null;
        }
    }

}
