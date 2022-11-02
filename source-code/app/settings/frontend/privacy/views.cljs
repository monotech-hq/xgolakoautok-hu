
(ns app.settings.frontend.privacy.views
    (:require [elements.api                   :as elements]
              [settings.cookie-settings.views :rename {body cookie-settings}]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn body
  [_]
  [cookie-settings])
