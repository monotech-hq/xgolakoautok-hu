
(ns project.ui.backend.loading-screen
    (:require [candy.api    :refer [param]]
              [css.api      :as css]
              [re-frame.api :as r]
              [x.user.api   :as x.user]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- progress-indicator
  [_]
  [:div {:style (css/unparse {:animation-duration        "3s"
                              :animation-name            "opacity-0-1-0"
                              :animation-iteration-count "infinite"
                              :animation-timing-function "cubic-bezier(1, 0, 0, 1)"
                              :font-weight               "500"
                              :line-height               "24px"
                              :color     (css/var "color-muted")
                              :font-size (css/var "font-size-xs")})}
        "Loading ..."])

(defn- app-title
  [_]
  (let [title @(r/subscribe [:x.core/get-app-config-item :app-title])]
       [:div {:style (css/unparse {:font-size           (css/var "font-size-xs")
                                   :font-weight         "600"
                                   :line-height         "24px"
                                   :padding             "12px 0"
                                   :text-align          "center"
                                   :text-transform      "uppercase"
                                   :user-select         "none"
                                   :-moz-user-select    "none"
                                   :-ms-user-select     "none"
                                   :-webkit-user-select "none"})}
             (param title)]))

(defn- app-logo
  [request]
  (let [selected-theme (x.user/request->user-settings-item request :selected-theme)]
       [:div {:style (css/unparse {:background-image (case selected-theme :light (css/url "/monotech/logo/logo-light.png")
                                                                          :dark  (css/url "/monotech/logo/logo-dark.png"))
                                   :background-position "bottom"
                                   :background-repeat   "no-repeat"
                                   :background-size     "contain"
                                   :height              "100px"
                                   :overflow            "hidden"
                                   :width               "100px"})}]))

(defn view
  [request]
  [:div {:style (css/unparse {:align-items :center :display :flex :flex-direction :column})}
        (app-logo           request)
        (app-title          request)
        (progress-indicator request)])
