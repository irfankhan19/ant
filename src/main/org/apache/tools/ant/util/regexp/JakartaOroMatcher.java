/*
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2000-2001 The Apache Software Foundation.  All rights 
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer. 
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution, if
 *    any, must include the following acknowlegement:  
 *       "This product includes software developed by the 
 *        Apache Software Foundation (http://www.apache.org/)."
 *    Alternately, this acknowlegement may appear in the software itself,
 *    if and wherever such third-party acknowlegements normally appear.
 *
 * 4. The names "The Jakarta Project", "Ant", and "Apache Software
 *    Foundation" must not be used to endorse or promote products derived
 *    from this software without prior written permission. For written 
 *    permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache"
 *    nor may "Apache" appear in their names without prior written
 *    permission of the Apache Group.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 * ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 */

package org.apache.tools.ant.util.regexp;

import org.apache.tools.ant.BuildException;
import org.apache.oro.text.regex.MatchResult;
import org.apache.oro.text.regex.Pattern;
import org.apache.oro.text.regex.Perl5Compiler;
import org.apache.oro.text.regex.Perl5Matcher;



import java.util.Vector;

/**
 * Implementation of RegexpMatcher for Jakarta-ORO.
 *
 * @author <a href="mailto:stefan.bodewig@epost.de">Stefan Bodewig</a> 
 * @author <a href="mailto:mattinger@mindless.com">Matthew Inger</a>
 */
public class JakartaOroMatcher implements RegexpMatcher {

    private String pattern;
    protected final Perl5Compiler compiler = new Perl5Compiler();
    protected final Perl5Matcher matcher = new Perl5Matcher();

    public JakartaOroMatcher() {}

    /**
     * Set the regexp pattern from the String description.
     */
    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    /**
     * Get a String representation of the regexp pattern
     */
    public String getPattern() {
        return this.pattern;
    }

    /**
     * Get a compiled representation of the regexp pattern
     */
    protected Pattern getCompiledPattern(int options)
        throws BuildException
    {
        try
        {
            // compute the compiler options based on the input options first
            Pattern p = compiler.compile(pattern, getCompilerOptions(options));
            return p;
        }
        catch (Exception e)
        {
            throw new BuildException(e);
        }
    }

    /**
     * Does the given argument match the pattern?
     */
    public boolean matches(String argument) throws BuildException {
        return matches(argument, MATCH_DEFAULT);
    }

    /**
     * Does the given argument match the pattern?
     */
    public boolean matches(String input, int options)
        throws BuildException
    {
        Pattern p = getCompiledPattern(options);
        return matcher.contains(input, p);
    }

    /**
     * Returns a Vector of matched groups found in the argument.
     *
     * <p>Group 0 will be the full match, the rest are the
     * parenthesized subexpressions</p>.
     */
    public Vector getGroups(String argument) throws BuildException {
        return getGroups(argument, MATCH_DEFAULT);
    }

    /**
     * Returns a Vector of matched groups found in the argument.
     *
     * <p>Group 0 will be the full match, the rest are the
     * parenthesized subexpressions</p>.
     */
    public Vector getGroups(String input, int options)
        throws BuildException
    {
        if (!matches(input, options)) {
            return null;
        }
        Vector v = new Vector();
        MatchResult mr = matcher.getMatch();
        int cnt = mr.groups();
        for (int i=0; i<cnt; i++) {
            v.addElement(mr.group(i));
        }
        return v;
    }

    protected int getCompilerOptions(int options)
    {
        int cOptions = Perl5Compiler.DEFAULT_MASK;

        if (RegexpUtil.hasFlag(options, MATCH_CASE_INSENSITIVE)) {
            cOptions |= Perl5Compiler.CASE_INSENSITIVE_MASK;
        }
        if (RegexpUtil.hasFlag(options, MATCH_MULTILINE)) {
            cOptions |= Perl5Compiler.MULTILINE_MASK;
        }
        if (RegexpUtil.hasFlag(options, MATCH_SINGLELINE)) {
            cOptions |= Perl5Compiler.SINGLELINE_MASK;
        }
        
        return cOptions;
    }

}
