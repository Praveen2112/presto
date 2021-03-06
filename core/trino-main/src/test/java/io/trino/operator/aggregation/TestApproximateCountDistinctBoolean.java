/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.trino.operator.aggregation;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.primitives.Booleans;
import io.trino.spi.type.Type;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static io.trino.spi.type.BooleanType.BOOLEAN;

public class TestApproximateCountDistinctBoolean
        extends AbstractTestApproximateCountDistinct
{
    @Override
    protected Type getValueType()
    {
        return BOOLEAN;
    }

    @Override
    protected Object randomValue()
    {
        return ThreadLocalRandom.current().nextBoolean();
    }

    @DataProvider(name = "inputSequences")
    public Object[][] inputSequences()
    {
        return new Object[][] {
                {true},
                {false},
                {true, false},
                {true, true, true},
                {false, false, false},
                {true, false, true, false},
        };
    }

    @Test(dataProvider = "inputSequences")
    public void testNonEmptyInputs(boolean... inputSequence)
    {
        List<Boolean> values = Booleans.asList(inputSequence);
        assertCount(values, 0, distinctCount(values));
    }

    @Test
    public void testNoInput()
    {
        assertCount(ImmutableList.of(), 0, 0);
    }

    private long distinctCount(List<Boolean> inputSequence)
    {
        return ImmutableSet.copyOf(inputSequence).size();
    }

    @Override
    protected int getUniqueValuesCount()
    {
        return 2;
    }
}
