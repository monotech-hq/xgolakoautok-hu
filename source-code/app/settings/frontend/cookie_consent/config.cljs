
(ns app.settings.frontend.cookie-consent.config)

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (ms)
;  BUG#2457
;  A cookie-consent popup nem jelenhet meg a legelőször megjelenített surface előtt!
(def BOOT-RENDERING-DELAY 1000)
