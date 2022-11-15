
(ns app.website-config.frontend.editor.lifecycles
    (:require [x.core.api :as x.core]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(x.core/reg-lifecycles! ::lifecycles
  {:on-app-boot {:dispatch-n [[:home.screen/add-menu-item! {:group-name :website
                                                            :icon       :tune
                                                            :icon-color "#8655b1"
                                                            :label      :website-config
                                                            :on-click   [:x.router/go-to! "/@app-home/website-config"]
                                                            :horizontal-weight 3}]
                              [:home.sidebar/add-menu-item! {:group-name :website
                                                             :icon       :tune
                                                             :icon-color "#8655b1"
                                                             :label      :website-config
                                                             :on-click   [:x.router/go-to! "/@app-home/website-config"]
                                                             :vertical-weight 3}]]}})
