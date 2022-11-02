
(ns app.views.frontend.privacy-policy.views
    (:require [app.common.frontend.api :as common]
              [elements.api            :as elements]
              [layouts.surface-a.api   :as surface-a]
              [re-frame.api            :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- view-structure
  [])

(defn view
  [surface-id]
  ; Remove stored cookies button
  ; Multilingual content
  ;
  ; WARNING!
  ; Az Adatvédelmi irányelvek tartalmát jelenítsd meg a cookie-consent popup felületen,
  ; ahelyett, hogy erre az oldalra irányítanád a privacy-policy gombbal a felhasználót!
  ; Erről az oldalról tovább lehet navigálni az applikáció más részire anélkül, hogy
  ; elfogadná a cookie-consent tartalmát!
  [surface-a/layout surface-id
                    {:content #'view-structure}])
