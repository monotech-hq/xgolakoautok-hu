
(ns app.website-link.frontend.lifecycles
    (:require [x.core.api :as x.core]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(x.core/reg-lifecycles! ::lifecycles
  {:on-app-boot {:dispatch-n [[:home.screen/add-menu-item! {:group-name :website
                                                            :icon       :computer
                                                            :icon-color "#8655b1"
                                                            :label      :view-website!
                                                            :on-click   [:website-link/load-menu!]
                                                            :horizontal-weight 1}]
                              [:home.sidebar/add-menu-item! {:group-name :website
                                                             :icon       :computer
                                                             :icon-color "#8655b1"
                                                             :label      :view-website!
                                                             :on-click   [:website-link/load-menu!]
                                                             :vertical-weight 1}]]}})
