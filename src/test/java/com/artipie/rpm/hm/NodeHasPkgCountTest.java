/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2020 artipie.com
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NON-INFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.artipie.rpm.hm;

import com.artipie.rpm.meta.XmlPackage;
import com.jcabi.xml.XMLDocument;
import java.io.FileNotFoundException;
import java.nio.file.Paths;
import org.hamcrest.Description;
import org.hamcrest.MatcherAssert;
import org.hamcrest.StringDescription;
import org.hamcrest.core.IsEqual;
import org.hamcrest.core.IsNot;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.llorllale.cactoos.matchers.Matches;

/**
 * Test for {@link NodeHasPkgCount}.
 * @since 0.10
 * @checkstyle MagicNumberCheck (500 lines)
 */
final class NodeHasPkgCountTest {

    /**
     * Wrong xml path.
     */
    private static final String WRONG =
        "src/test/resources-binary/repodata/wrong-package.xml.example";

    /**
     * Primary xml example path.
     */
    private static final String PRIMARY =
        "src/test/resources-binary/repodata/primary.xml.example";

    @Test
    void countsPackages() throws FileNotFoundException {
        MatcherAssert.assertThat(
            new NodeHasPkgCount(2, XmlPackage.OTHER.tag()),
            new Matches<>(
                new XMLDocument(
                    Paths.get("src/test/resources-binary/repodata/other.xml.example")
                )
            )
        );
    }

    @Test
    void doesNotMatchWhenPackagesAmountDiffers() throws FileNotFoundException {
        MatcherAssert.assertThat(
            new NodeHasPkgCount(10, XmlPackage.PRIMARY.tag()),
            new IsNot<>(
                new Matches<>(
                    new XMLDocument(
                        Paths.get(NodeHasPkgCountTest.PRIMARY)
                    )
                )
            )
        );
    }

    @Test
    @Disabled
    void describesCorrectlyWhenPackagesAmountDiffers() throws FileNotFoundException {
        final Description desc = new StringDescription();
        new NodeHasPkgCount(10, XmlPackage.PRIMARY.tag(), desc).matches(
            new XMLDocument(
                Paths.get(NodeHasPkgCountTest.PRIMARY)
            )
        );
        MatcherAssert.assertThat(
            desc.toString(),
            new IsEqual<>(
                "2 packages found, `packages` attribute value is 2, expected 10 package count"
            )
        );
    }

    @Test
    @Disabled
    void doesNotMatchWhenPackageAttributeDiffers() throws FileNotFoundException {
        MatcherAssert.assertThat(
            new NodeHasPkgCount(2, XmlPackage.OTHER.tag()),
            new IsNot<>(
                new Matches<>(
                    new XMLDocument(
                        Paths.get(NodeHasPkgCountTest.WRONG)
                    )
                )
            )
        );
    }

    @Test
    @Disabled
    void describesCorrectlyWhenPackageAttributeDiffers() throws FileNotFoundException {
        final Description desc = new StringDescription();
        new NodeHasPkgCount(
            2,
            XmlPackage.OTHER.tag(),
            desc
        ).matches(
            new XMLDocument(
                Paths.get(NodeHasPkgCountTest.WRONG)
            )
        );
        MatcherAssert.assertThat(
            desc.toString(),
            new IsEqual<>(
                "2 packages found, `packages` attribute value is 3, expected 2 package count"
            )
        );
    }
}
