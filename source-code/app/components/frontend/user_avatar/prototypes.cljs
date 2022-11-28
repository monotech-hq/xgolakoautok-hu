
(ns app.components.frontend.user-avatar.prototypes
    (:require [candy.api    :refer [param]]
              [re-frame.api :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn avatar-props-prototype
  ; @param (map) avatar-props
  ;  {:first-name (string)(opt)
  ;   :last-name (string)(opt)}
  ;
  ; @return (map)
  ;  {:colors (strings in vector)
  ;   :initials (string)
  ;   :size (px)}
  [{:keys [first-name last-name] :as avatar-props}]
  (merge {:colors    ["var( --color-muted )"]
          :initials @(r/subscribe [:x.locales/get-ordered-initials first-name last-name])
          :size 60}
         (param avatar-props)))
