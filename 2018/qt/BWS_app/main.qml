import QtQuick 2.7
import QtQuick.Controls 2.0
import QtQuick.Layouts 1.0
import io.backend 1.0

ApplicationWindow {
    visible: true
    width: 580
    height: 960
    title: qsTr("Hello World")

    BackEnd {
        id: backend
    }

    Page1 {
    }

}
