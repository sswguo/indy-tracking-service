/**
 * Copyright (C) 2023 Red Hat, Inc. (https://github.com/Commonjava/indy-tracking-service)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.commonjava.indy.service.tracking.exception;

import java.text.MessageFormat;

public class IndyLifecycleException
                extends Exception
{
    private static final long serialVersionUID = 1L;

    private Object[] params;

    private String formattedMessage;

    public IndyLifecycleException( final String message, final Throwable cause, final Object... params )
    {
        super( message, cause );
        this.params = params;
    }

    public IndyLifecycleException( final String message, final Object... params )
    {
        super( message );
        this.params = params;
    }

    @Override
    public synchronized String getMessage()
    {
        if ( formattedMessage == null )
        {
            final String format = super.getMessage().replaceAll( "\\{\\}", "%s" );
            if ( params == null || params.length < 1 )
            {
                formattedMessage = format;
            }
            else
            {
                final String original = formattedMessage;
                try
                {
                    formattedMessage = String.format( format, params );
                }
                catch ( final Error | Exception e )
                {
                    // do nothing
                }

                if ( formattedMessage == null || formattedMessage.equals( original ) )
                {
                    try
                    {
                        formattedMessage = MessageFormat.format( format, params );
                    }
                    catch ( final Error | RuntimeException e )
                    {
                        formattedMessage = format;
                        throw e;
                    }
                    catch ( final Exception e )
                    {
                        formattedMessage = format;
                    }
                }
            }
        }

        return formattedMessage;
    }

    private Object writeReplace()
    {
        final Object[] newParams = new Object[params.length];
        int i = 0;
        for ( final Object object : params )
        {
            newParams[i] = String.valueOf( object );
            i++;
        }

        this.params = newParams;
        return this;
    }

}
