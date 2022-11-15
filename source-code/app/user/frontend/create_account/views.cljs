
(ns app.user.frontend.create-account.views
    (:require [elements.api :as elements]
              [re-frame.api :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- create-account-form
  [])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- logged-in-form
  [])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- body
  []
  (let [viewport-small? @(r/subscribe [:x.environment/viewport-small?])]
       [:div#create-account--body {:style (if viewport-small? {:width         "320px"}
                                                              {:border-color  "var( --border-color-highlight )"
                                                               :border-radius "var( --border-radius-m )"
                                                               :border-style  "solid"
                                                               :border-width  "1px"
                                                               :width         "320px"})}
                                  (if-let [user-identified? @(r/subscribe [:x.user/user-identified?])]
                                          [logged-in-form]
                                          [create-account-form])]))

(defn- create-account
  []
  [elements/label {:content "Create account"
                   :line-height :block}])

(defn view
  [surface-id]
  [create-account])
