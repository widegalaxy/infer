/*
* Copyright (c) 2013- Facebook.
* All rights reserved.
*/

package endtoend.java.eradicate;


import static org.hamcrest.MatcherAssert.assertThat;
import static utils.matchers.ErrorPattern.createPatterns;
import static utils.matchers.ResultContainsExactly.containsExactly;

import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import utils.InferException;
import utils.InferResults;
import utils.matchers.ErrorPattern;

public class ReturnNotNullableTest {


  public static final String SOURCE_FILE =
      "infer/tests/codetoanalyze/java/eradicate/ReturnNotNullable.java";

  public static final String RETURN_NOT_NULLABLE =
      "ERADICATE_RETURN_NOT_NULLABLE";
  public static final String CONDITION_REDUNDANT_NONNULL =
      "ERADICATE_CONDITION_REDUNDANT_NONNULL";
  public static final String RETURN_OVER_ANNOTATED =
      "ERADICATE_RETURN_OVER_ANNOTATED";


  private static InferResults inferResults;

  @BeforeClass
  public static void loadResults() throws InterruptedException, IOException {
    inferResults =
      InferResults.loadEradicateResults(ReturnNotNullableTest.class, SOURCE_FILE);
  }

  boolean returnOverAnnotatedEnabled = true;

  @Test
  public void matchErrors()
      throws IOException, InterruptedException, InferException {


    String[] nullableMethods = {"returnNull", "returnNullable"};
    List<ErrorPattern> errorPatterns = createPatterns(
        RETURN_NOT_NULLABLE,
        SOURCE_FILE,
        nullableMethods);

    String[] redundantMethods = {"redundantEq", "redundantNeq"};
    errorPatterns.addAll(
        createPatterns(
            CONDITION_REDUNDANT_NONNULL,
            SOURCE_FILE,
            redundantMethods
        )
    );

    if (returnOverAnnotatedEnabled) {
        errorPatterns.addAll(
            createPatterns(
                RETURN_OVER_ANNOTATED,
                SOURCE_FILE,
                redundantMethods
            )
        );
    }

    assertThat(
        "Results should contain ",
        inferResults,
        containsExactly(errorPatterns)
    );
  }

}
