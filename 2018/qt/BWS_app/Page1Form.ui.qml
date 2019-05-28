import QtQuick 2.7
import QtQuick.Controls 2.0
import QtQuick.Layouts 1.0

Item {
    id: item1
    property alias text1: text1
    property alias text2: text2
    property alias text3: text3
    property alias text4: text4
    property alias radioButton: radioButton
    property alias radioButton1: radioButton1
    property alias radioButton2: radioButton2
    property alias radioButton3: radioButton3
    property alias radioButton4: radioButton4
    property alias radioButton5: radioButton5
    property alias radioButton6: radioButton6
    property alias radioButton7: radioButton7
    property alias submit: submit

    Rectangle {
        id: rectangle1
        x: 51
        y: 510
        width: 474
        height: 114
        color: "#bbb6b6"
        border.width: 0
    }
    Rectangle {
        id: rectangle
        x: 51
        y: 621
        width: 474
        height: 114
        color: "#ffffff"
        border.width: 0
    }

    Rectangle {
        id: rectangle2
        x: 51
        y: 399
        width: 474
        height: 114
        color: "#ffffff"
        border.width: 0
    }

    Rectangle {
        id: rectangle3
        x: 51
        y: 285
        width: 474
        height: 114
        color: "#bbb6b6"
        border.width: 0
    }

    Grid {
        id: grid
        x: 63
        y: 323
        width: 400
        height: 400
        spacing: 1
        rows: 4
        columns: 3

        Column {
            id: best
            width: 200
            height: 400
            spacing: 70

            RadioButton {
                id: radioButton
                spacing: 7
                padding: 7
                rightPadding: 7
                bottomPadding: 7
                leftPadding: 7
                topPadding: 7
            }

            RadioButton {
                id: radioButton1
                rightPadding: 7
                leftPadding: 7
                spacing: 7
                topPadding: 7
                bottomPadding: 7
                padding: 7
            }

            RadioButton {
                id: radioButton2
                rightPadding: 7
                leftPadding: 7
                spacing: 7
                topPadding: 7
                bottomPadding: 7
                padding: 7
            }

            RadioButton {
                id: radioButton3
                rightPadding: 7
                leftPadding: 7
                spacing: 7
                topPadding: 7
                bottomPadding: 7
                padding: 7
            }
        }

        Column {
            id: option
            x: 201
            y: 0
            width: 200
            height: 400
            spacing: 90

            Text {
                id: text4
                text: qsTr("Text1")
                font.bold: false
                horizontalAlignment: Text.AlignHCenter
                style: Text.Normal
                font.pixelSize: 20
            }

            Text {
                id: text3
                text: qsTr("Text2")
                horizontalAlignment: Text.AlignHCenter
                font.pixelSize: 20
            }

            Text {
                id: text2
                text: qsTr("Text3")
                horizontalAlignment: Text.AlignHCenter
                font.pixelSize: 20
            }

            Text {
                id: text1
                text: qsTr("Text4")
                horizontalAlignment: Text.AlignHCenter
                font.pixelSize: 20
            }
        }

        Column {
            id: worst
            width: 200
            height: 400
            spacing: 70
            RadioButton {
                id: radioButton4
                rightPadding: 7
                leftPadding: 7
                spacing: 7
                topPadding: 7
                bottomPadding: 7
                padding: 7
            }

            RadioButton {
                id: radioButton5
                rightPadding: 7
                leftPadding: 7
                spacing: 7
                topPadding: 7
                padding: 7
                bottomPadding: 7
            }

            RadioButton {
                id: radioButton6
                rightPadding: 7
                leftPadding: 7
                spacing: 7
                topPadding: 7
                padding: 7
                bottomPadding: 7
            }

            RadioButton {
                id: radioButton7
                rightPadding: 7
                leftPadding: 7
                spacing: 7
                topPadding: 7
                padding: 7
                bottomPadding: 7
            }
        }
    }

    Text {
        id: instructions_two
        x: 191
        y: 131
        text: qsTr("and worst option.")
        style: Text.Sunken
        wrapMode: Text.WordWrap
        font.pixelSize: 25
        bottomPadding: 0
    }

    Text {
        id: instructions_one
        x: 134
        y: 89
        text: qsTr("Please choose a best option")
        bottomPadding: 0
        style: Text.Sunken
        wrapMode: Text.WordWrap
        font.pixelSize: 25
    }

    Text {
        id: b_header
        x: 120
        y: 246
        text: qsTr("BEST")
        font.pixelSize: 21
    }

    Text {
        id: w_header
        x: 394
        y: 246
        text: qsTr("WORST")
        font.pixelSize: 21
    }

    Button {
        id: submit
        x: 238
        y: 826
        width: 100
        text: qsTr("NEXT")
        bottomPadding: 7
        topPadding: 7
    }
}
