# qupath-extension-neuror
목표: NeuroR GUI를 생성하여 QuPath extension으로 실행하고자 합니다. 

1. 초기에는 NeuroR script를 여는 extension을 만들었습니다. (qupath-extension-neuror-script.jar)

extension (확장자 .jar) 파일의 필수 요소는 아래와 같습니다. 
- META-INF/MANIFEST.MF
- META-INF/services/qupath.lib.gui.extensions.QuPathExtension
  -> qupath.lib.gui.extensions.QuPathExtension 파일 안에는 "qupath.lib.neuror.NEURORExtension" 내용이 반드시 기재되어 있어야 합니다.
- qupath/lib/neuror/NEURORExtension.class
- scripts/run_segmentation.groovy
원리는 NEURORExtension.class 가 scripts 폴더 안에 있는 run_segmentation.groovy 를 불러옵니다. 


2. 현재 목표하는 것은 extension에서 GUI 창을 불러오고, 해당 창에서 folder path 및 parameter를 설정하고 "Run" 버튼으로 실행까지 하고자 합니다.

제가 만들어둔 것은 크게 아래와 같은 구조를 가집니다.
- sample.fxml : fxml 언어로 GUI 를 구성합니다. (scene builder 프로그램으로 만들었습니다)
- NeuroRApplication : sample.fxml 파일을 불러옵니다.
- NeuroRController : sample.fxml 에 작성된 버튼 또는 텍스트박스 등의 로직을 정의합니다. 

3. 문제점

1번 항목을 참조하여 2번 항목 파일을 .jar 파일로 만들어 extension으로 실행하면 메뉴에서 클릭을 해도 GUI 창이 실행되지 않습니다. 


4. 문제해결 시도
javaFX로 GUI를 구성하는 방법은 크게 2가지가 있다고 합니다. 
- fxml 을 사용하는 방법 (시각적으로 디자인할 수 있어 개발자가 UI를 더 쉽게 구성할 수 있습니다)
- Java 코드로 직접 구성하는 방법 (fxml을 사용하지 않으므로 추가적인 파일 관리가 필요 없습니다)

QuPath 상에서 fxml 파일이 열리지 않을 수도 있다고 생각해서 java 코드로 직접 작성한 케이스도 해 보았지만 동일한 현상으로 실패하였습니다. 


