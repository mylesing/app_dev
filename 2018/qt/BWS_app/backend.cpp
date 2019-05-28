#include "backend.h"

BackEnd::BackEnd(QObject *parent) :
    QObject(parent)
{
}

QList<QString> BackEnd::getOptions()
{
    m_options.append("a");
    m_options.append("b");
    return m_options;
}

void BackEnd::update(const QList<QString> bw)
{
    m_options.clear();
    m_options.append(bw.at(0));
    m_options.append(bw.at(1));

}
