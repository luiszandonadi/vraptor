/***
 * Copyright (c) 2009 Caelum - www.caelum.com.br/opensource
 * All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 * 
 * 	http://www.apache.org/licenses/LICENSE-2.0 
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and 
 * limitations under the License. 
 */
package br.com.caelum.vraptor.reflection;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import br.com.caelum.vraptor.http.ParameterNameProvider;
import br.com.caelum.vraptor.http.TypeCreator;
import br.com.caelum.vraptor.resource.DefaultResourceMethod;
import br.com.caelum.vraptor.resource.ResourceMethod;

public class CacheBasedTypeCreatorTest {

    private Mockery mockery;
    private ResourceMethod method;
    private CacheBasedTypeCreator creator;
    private TypeCreator delegate;

    @Before
    public void setup() {
        this.mockery = new Mockery();
        this.method = mockery.mock(ResourceMethod.class);
        this.delegate = mockery.mock(TypeCreator.class);
        this.creator = new CacheBasedTypeCreator(delegate, mockery.mock(ParameterNameProvider.class));

		mockery.checking(new Expectations() {
			{
				allowing(method).getMethod();
				will(returnValue(TypeCreator.class.getMethods()[0]));
			}
		});
    }

    @Test
    public void shouldUseTheSameInstanceIfRequiredTwice() {
        mockery.checking(new Expectations() {
            {
                one(delegate).typeFor(method); will(returnValue(String.class));
            }
        });
        Class<?> firstResult = creator.typeFor(method);
        Class<?> secondResult = creator.typeFor(DefaultResourceMethod.instanceFor(TypeCreator.class, TypeCreator.class.getMethods()[0]));
        Assert.assertEquals(firstResult, secondResult);
        mockery.assertIsSatisfied();
    }

}
