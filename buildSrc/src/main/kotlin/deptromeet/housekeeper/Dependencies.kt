package deptromeet.housekeeper

object Dependencies {
  private const val CORE_KTX = "1.7.0"
  const val coreKtx = "androidx.core:core-ktx:$CORE_KTX"

  private const val APPCOMPAT = "1.4.1"
  const val appcompat = "androidx.appcompat:appcompat:$APPCOMPAT"

  private const val MATERIAL = "1.5.0"
  const val material = "com.google.android.material:material:$MATERIAL"

  private const val CONSTRAINT_LAYOUT = "2.1.3"
  const val constraintLayout = "androidx.constraintlayout:constraintlayout:$CONSTRAINT_LAYOUT"

  private const val JUNIT = "4.13.2"
  const val junit = "junit:junit:$JUNIT"

  private const val TEST_EXT_JUNIT = "1.1.3"
  const val testExtJunit = "androidx.test.ext:junit:$TEST_EXT_JUNIT"

  private const val ESPRESSO_CORE = "3.4.0"
  const val espressoCore = "androidx.test.espresso:espresso-core:$ESPRESSO_CORE"
}