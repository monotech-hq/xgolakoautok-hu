
(ns app.settings.frontend.api
    (:require ;[settings.appearance-settings.effects]
              ;[settings.appearance-settings.views]
              ;[settings.cookie-consent.effects]
              ;[settings.cookie-consent.lifecycles]
              ;[settings.cookie-consent.subs]
              ;[settings.cookie-consent.views]
              ;[settings.cookie-settings.effects]
              ;[settings.cookie-settings.views]
              ;[settings.notification-settings.views]
              ;[settings.personal-settings.views]
              ;[settings.privacy-settings.views]
              ;[settings.remove-stored-cookies.effects]
              ;[settings.remove-stored-cookies.views]
              [app.settings.frontend.appearance.lifecycles]
              [app.settings.frontend.editor.events]
              [app.settings.frontend.handler.effects]
              [app.settings.frontend.lifecycles]
              [app.settings.frontend.notifications.lifecycles]
              ;[app.settings.frontend.personal.lifecycles]
              [app.settings.frontend.privacy.lifecycles]
              [app.settings.frontend.sales.lifecycles]



              ; Az *.editor.effects névtér helyett a *.blank.effects névtéret
              ; használd, ha a projektnek nincsenek beállításai!
              [app.settings.frontend.editor.effects]))
             ;[app.settings.frontend.blank.effects]))
