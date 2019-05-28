#ifndef BACKEND_H
#define BACKEND_H

#include <QObject>
#include <QString>

class BackEnd : public QObject
{
    Q_OBJECT
    Q_PROPERTY(QList<QString> options READ getOptions)
    Q_PROPERTY(QList<QString> bw WRITE update)

public:
    explicit BackEnd(QObject *parent = nullptr);

    QList<QString> getOptions();
    void update(const QList<QString> bw);

private:
    QList<QString> m_options;
};

#endif // BACKEND_H
